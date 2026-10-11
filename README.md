PS5UpdateBlocker
A lightweight proxy and network-level filtering tool designed to block PlayStation 5 system software update requests and preserve target firmware versions.
Overview
 * Firmware Preservation: Keeps the console on its current firmware version for exploit/jailbreak compatibility.
 * Update Interception: Drops and blocks incoming update manifests and payload requests via an HTTP/HTTPS proxy.
 * Telemetry Filtering: Restricts diagnostic and analytics traffic to Sony servers.
Prerequisites
 * An Android device running the PS5UpdateBlocker application, connected to the same local network (Wi-Fi/Ethernet) as the PS5.
 * The local IPv4 address of the Android device (e.g., 192.168.1.50).
 * The listening proxy port configured in the app (default is usually 8080).
Configuration & Usage
1. Start the Android Application
 * Launch PS5UpdateBlocker on your Android device.
 * Note the displayed IP Address and Port.
 * Tap Start to initiate the local proxy server.
2. Configure PS5 Network Settings
To route traffic through the blocking proxy, configure the console's network settings manually:
 * On the PS5, go to Settings > Network > Settings > Set Up Internet Connection.
 * Highlight your active network connection (Wi-Fi or LAN), press the Options button on the DualSense controller, and select Advanced Settings.
 * Adjust the following parameters:
   * Proxy Server: Change from Do Not Use to Use.
   * Address: Enter the local IP address of your Android device (e.g., 192.168.1.50).
   * Port Number: Enter the active port specified in the app (e.g., 8080).
 * Select OK to save the changes and reconnect.
3. Disable Automatic Console Updates
Navigate to Settings > System > System Software > System Software Updates and Settings and disable both:
 * Download Update Files Automatically -> Disabled
 * Install Update Files Automatically -> Disabled
Verification
 * Go to Settings > Network > Connection Status > Test Internet Connection.
 * Check for system software updates manually via Settings > System > System Software Update.
 * The console should fail to reach update endpoints, returning a connection error (e.g., CE-108853-5), while general network connectivity remains operational.
Disclaimer
 * Blocking update endpoints restricts access to PlayStation Network (PSN) features, online multiplayer, and the PlayStation Store.
 * The proxy host device must remain powered on and connected to the network while the PS5 is in use.
