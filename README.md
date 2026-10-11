# PS5UpdateBlocker

A lightweight Android-based local proxy server specifically designed to intercept and block PlayStation 5 system software update requests, keeping your console securely on its current firmware.

---

## Features

* **Firmware Retention:** Prevents unwanted background downloads and forced system software updates.
* **No Router/DNS Hassle:** Works entirely via an Android device—no router root access, custom firmware, or external Pi-hole setup required.
* **Telemetry & Tracking Block:** Drops outbound analytics and telemetry requests bound for Sony servers.
* **Lightweight & Fast:** Runs as a minimal background proxy service on any modern Android device.

---

## How It Works

PS5UpdateBlocker spins up a local HTTP/HTTPS proxy on your Android device. When the PS5 routes its traffic through this proxy, any requests targeting known PlayStation update manifests, CDNs, and telemetry hosts are intercepted and dropped, while non-update network traffic continues to pass through.

### Blocked Target Hosts
* `fus01.ps5.update.playstation.net`
* `fuk01.ps5.update.playstation.net`
* `dus01.ps5.update.playstation.net`
* `duk01.ps5.update.playstation.net`
* `telemetry.api.playstation.com`
* `telemetry-ingest.api.playstation.com`

---

## Requirements

* An Android phone or tablet connected to the **same local network (Wi-Fi)** as your PS5.
* PlayStation 5 console.

---

## Setup & Usage Guide

### Step 1: Run the App on Android
1. Download and install the latest APK from the **[Releases](../../releases)** tab.
2. Open **PS5UpdateBlocker**.
3. Tap **Start Proxy**.
4. Take note of the **IP Address** and **Port** displayed on the screen (e.g., `192.168.1.50` : `8080`).

> **Note:** Ensure battery optimization is disabled for this app so Android does not kill the proxy service in the background.

---

### Step 2: Configure Network Settings on PS5
1. Go to **Settings** > **Network** > **Settings** > **Set Up Internet Connection**.
2. Highlight your current Wi-Fi or LAN connection.
3. Press the **Options** button on your controller and select **Advanced Settings**.
4. Configure the proxy parameters:
   * **Proxy Server:** Select **Use**.
   * **Address:** Enter your Android device's IP address.
   * **Port Number:** Enter the active port shown in the app (e.g., `8080`).
5. Select **OK** to save and apply the settings.

---

### Step 3: Disable Native Auto-Updates
As a secondary safety layer, disable automatic downloads directly on the console:
1. Go to **Settings** > **System** > **System Software** > **System Software Updates and Settings**.
2. Turn off **Download Update Files Automatically**.
3. Turn off **Install Update Files Automatically**.

---

## Verification

To verify that the blocker is actively working:
1. On your PS5, navigate to **Settings** > **System** > **System Software** > **System Software Update and Settings** > **Update System Software**.
2. Attempt to check for an update over the internet.
3. The console should fail to contact the update servers and return an error (such as `CE-108853-5`), confirming the requests are being successfully blocked.

---

## Disclaimer & Legal Notice

* **Non-Affiliation:** This project is an independent open-source utility and is not affiliated, associated, authorized, endorsed by, or in any way officially connected with Sony Group Corporation, Sony Interactive Entertainment Inc., or any of their subsidiaries or affiliates.
* **Trademarks:** "PlayStation", "PS5", "DualSense", and related marks are registered trademarks of Sony Interactive Entertainment Inc. All product and company names are trademarks™ or registered® trademarks of their respective holders. Their use here does not imply any affiliation with or endorsement by them.
* **PSN Availability:** Blocking update endpoints restricts access to PlayStation Network (PSN) services, online matchmaking, and the PlayStation Store.
* **No Warranty:** This software is provided "as is", without warranty of any kind, express or implied. Users assume all responsibility and risk regarding network configuration, account status, or console software management.

---

## License

Distributed under the GPL 3.0 License. See `LICENSE` for more information.

