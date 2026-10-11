PS5UpdateBlocker
A lightweight DNS and network-level filtering ruleset designed to prevent PlayStation 5 consoles from discovering, downloading, and installing system software updates.
Overview
 * Firmware Preservation: Keeps the console on its current firmware version for exploit/jailbreak stability.
 * Update Prevention: Blocks background and automatic system software downloads.
 * Telemetry Filtering: Restricts outbound analytics and diagnostic traffic to Sony servers.
Blocked Domains
# PS5 System Software Update Endpoints
fus01.ps5.update.playstation.net
fuk01.ps5.update.playstation.net
dus01.ps5.update.playstation.net
duk01.ps5.update.playstation.net

# Telemetry & Diagnostics
telemetry.api.playstation.com
telemetry-ingest.api.playstation.com

Deployment Methods
1. Pi-hole / AdGuard Home
 * Open the admin interface.
 * Navigate to Filters > DNS Blocklists (or Blacklist / Custom Filtering Rules).
 * Import or append the domain list (blocklist.txt).
 * Save changes and flush the DNS cache.
2. Router-Level Domain / Firewall Blocking
 * Access the router gateway interface.
 * Locate Domain Blocking, URL Filtering, or Access Control.
 * Add the domains listed above with an action set to Drop / Reject.
3. Console Settings Configuration
For complete mitigation, disable automatic updates in the PS5 settings:
 * Go to Settings > System > System Software > System Software Updates and Settings.
 * Set Download Update Files Automatically to Disabled.
 * Set Install Update Files Automatically to Disabled.
Verification
 * On the PS5, navigate to Settings > Network > Connection Status.
 * Run a network connection test.
 * Check for system updates manually:
   * The console should fail to connect to the update server and return a network/DNS error (e.g., CE-108853-5).
Disclaimer
 * Blocking update endpoints will restrict access to PlayStation Network (PSN) features, game downloads, and online multiplayer.
 * This ruleset is intended solely for firmware version retention and offline usage.
