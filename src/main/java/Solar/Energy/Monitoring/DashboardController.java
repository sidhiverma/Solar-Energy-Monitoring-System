package Solar.Energy.Monitoring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final SystemSettingRepository systemSettingRepository;
    private final EnergyReadingRepository energyReadingRepository;

    public DashboardController(UserRepository userRepository,
                               DeviceRepository deviceRepository,
                               SystemSettingRepository systemSettingRepository,
                               EnergyReadingRepository energyReadingRepository) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.systemSettingRepository = systemSettingRepository;
        this.energyReadingRepository = energyReadingRepository;
    }


    @GetMapping("/homeowner")
    public String homeowner(Model model) {

        // Read saved energy readings from the database.
        List<EnergyReading> readings =
                energyReadingRepository.findAllByOrderByIdAsc();

        // Add sample readings only when the database is empty.
        if (readings.isEmpty()) {
            List<EnergyReading> demoReadings = List.of(
                    new EnergyReading("08:00", 0.1, 0.3, 78),
                    new EnergyReading("10:00", 0.5, 0.4, 78),
                    new EnergyReading("12:00", 1.2, 0.5, 78),
                    new EnergyReading("14:00", 1.8, 0.6, 78),
                    new EnergyReading("16:00", 1.5, 0.7, 78),
                    new EnergyReading("18:00", 0.7, 0.7, 78)
            );

            readings = energyReadingRepository.saveAll(demoReadings);
        }

        // Calculate totals using the saved readings.
        double production = readings.stream()
                .mapToDouble(EnergyReading::getProduction)
                .sum();

        double consumption = readings.stream()
                .mapToDouble(EnergyReading::getConsumption)
                .sum();

        consumption = Math.round(consumption * 10.0) / 10.0;

        int battery = readings.get(readings.size() - 1).getBattery();

        // Read the administrator's saved battery threshold.
        int batteryThreshold = systemSettingRepository
                .findBySettingName("batteryThreshold")
                .map(setting -> Integer.parseInt(setting.getSettingValue()))
                .orElse(20);

        String alertMessage;

        if (battery <= batteryThreshold) {
            alertMessage =
                    "Warning: Battery level is at or below the alert threshold!";
        } else if (battery < 50) {
            alertMessage = "Battery level is getting low. Save energy.";
        } else {
            alertMessage =
                    "Battery level is healthy. Your system is operating normally.";
        }

        // Send readings to the dashboard for the chart.
        model.addAttribute("readingLabels",
                readings.stream()
                        .map(EnergyReading::getReadingTime)
                        .toList());

        model.addAttribute("productionData",
                readings.stream()
                        .map(EnergyReading::getProduction)
                        .toList());

        model.addAttribute("consumptionData",
                readings.stream()
                        .map(EnergyReading::getConsumption)
                        .toList());

        model.addAttribute("production", production);
        model.addAttribute("consumption", consumption);
        model.addAttribute("consumptionDisplay",
                String.format(java.util.Locale.US, "%.1f", consumption));
        model.addAttribute("battery", battery);
        model.addAttribute("balance",
                String.format("%.1f", production - consumption));
        model.addAttribute("status", "Online");
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("batteryThreshold", batteryThreshold);
        model.addAttribute("devices", deviceRepository.findAll());
        model.addAttribute("users", userRepository.findAll());

        return "homeowner";
    }



    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        SystemSetting setting = systemSettingRepository
                .findBySettingName("batteryThreshold")
                .orElseGet(() -> systemSettingRepository.save(
                        new SystemSetting("batteryThreshold", "20")));

        model.addAttribute("batteryThreshold", setting.getSettingValue());
        return "admin";
    }

    @PostMapping("/admin/users/add")
    public String addUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String role,
            RedirectAttributes redirectAttributes) {

        User user = new User(name, email, role);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute(
                "message", "User added successfully!");

        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String editUser(
            @PathVariable Long id, Model model) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid user ID"));

        model.addAttribute("user", user);

        return "edit-user";
    }

    @PostMapping("/admin/users/update/{id}")
    public String updateUser(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String role) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid user ID"));

        user.setName(name);
        user.setEmail(email);
        user.setRole(role);

        userRepository.save(user);

        return "redirect:/admin/users";
    }
    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid user ID"));

        userRepository.delete(user);

        redirectAttributes.addFlashAttribute(
                "message", "User deleted successfully!");

        return "redirect:/admin/users";
    }
    @PostMapping("/admin/devices/add")
    public String addDevice(
            @RequestParam String deviceId,
            @RequestParam String location,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        Device device = new Device(deviceId, location, status);
        deviceRepository.save(device);

        redirectAttributes.addFlashAttribute(
                "message", "Device registered successfully!");

        return "redirect:/admin";
    }
    @PostMapping("/admin/devices/delete/{id}")
    public String deleteDevice(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        Device device = deviceRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid device ID"));

        deviceRepository.delete(device);

        redirectAttributes.addFlashAttribute(
                "message", "Device deleted successfully!");

        return "redirect:/admin";
    }
    @GetMapping("/admin/devices/edit/{id}")
    public String editDevice(@PathVariable Long id, Model model) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid device ID"));

        model.addAttribute("device", device);
        return "edit-device";
    }

    @PostMapping("/admin/devices/update/{id}")
    public String updateDevice(
            @PathVariable Long id,
            @RequestParam String deviceId,
            @RequestParam String location,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        Device device = deviceRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid device ID"));

        device.setDeviceId(deviceId);
        device.setLocation(location);
        device.setStatus(status);

        deviceRepository.save(device);

        redirectAttributes.addFlashAttribute(
                "message", "Device updated successfully!");

        return "redirect:/admin";
    }
    @PostMapping("/admin/settings/battery-threshold")
    public String updateBatteryThreshold(
            @RequestParam int batteryThreshold,
            RedirectAttributes redirectAttributes) {

        if (batteryThreshold < 0 || batteryThreshold > 100) {
            redirectAttributes.addFlashAttribute(
                    "message", "Threshold must be between 0 and 100.");
            return "redirect:/admin";
        }

        SystemSetting setting = systemSettingRepository
                .findBySettingName("batteryThreshold")
                .orElseGet(() -> new SystemSetting(
                        "batteryThreshold",
                        String.valueOf(batteryThreshold)));

        setting.setSettingValue(String.valueOf(batteryThreshold));
        systemSettingRepository.save(setting);

        redirectAttributes.addFlashAttribute(
                "message", "Battery alert threshold updated successfully!");

        return "redirect:/admin";
    }

    @GetMapping("/admin/users")
    public String usersPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        return "users";
    }

    @GetMapping("/admin/devices")
    public String devicesPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        return "devices";
    }
}
