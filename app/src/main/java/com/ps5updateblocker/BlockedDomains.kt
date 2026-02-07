package com.ps5updateblocker

object BlockedDomains {
    
    val DOMAINS = setOf(
        "dau01.ps5.update.playstation.net",
        "dbr01.ps5.update.playstation.net",
        "dcn01.ps5.update.playstation.net",
        "deu01.ps5.update.playstation.net",
        "dhk01.ps5.update.playstation.net",
        "djp01.ps5.update.playstation.net",
        "dkr01.ps5.update.playstation.net",
        "dmx01.ps5.update.playstation.net",
        "dru01.ps5.update.playstation.net",
        "dsa01.ps5.update.playstation.net",
        "dtw01.ps5.update.playstation.net",
        "duk01.ps5.update.playstation.net",
        "dus01.ps5.update.playstation.net",
        "fau01.ps5.update.playstation.net",
        "fbr01.ps5.update.playstation.net",
        "fcn01.ps5.update.playstation.net",
        "feu01.ps5.update.playstation.net",
        "fhk01.ps5.update.playstation.net",
        "fjp01.ps5.update.playstation.net",
        "fkr01.ps5.update.playstation.net",
        "fmx01.ps5.update.playstation.net",
        "fru01.ps5.update.playstation.net",
        "fsa01.ps5.update.playstation.net",
        "ftw01.ps5.update.playstation.net",
        "fuk01.ps5.update.playstation.net",
        "fus01.ps5.update.playstation.net",
        "hau01.ps5.update.playstation.net",
        "hbr01.ps5.update.playstation.net",
        "hcn01.ps5.update.playstation.net",
        "heu01.ps5.update.playstation.net",
        "hhk01.ps5.update.playstation.net",
        "hjp01.ps5.update.playstation.net",
        "hkr01.ps5.update.playstation.net",
        "hmx01.ps5.update.playstation.net",
        "hru01.ps5.update.playstation.net",
        "hsa01.ps5.update.playstation.net",
        "htw01.ps5.update.playstation.net",
        "huk01.ps5.update.playstation.net",
        "hus01.ps5.update.playstation.net",
        "sgst.prod.dl.playstation.net",
        "gs2.ww.prod.dl.playstation.net"
    )
    
    // Domain ve alt domainleri kontrol et
    fun isBlocked(domain: String): Boolean {
        val lowerDomain = domain.lowercase().trim()
        
        // Tam eşleşme kontrolü
        if (lowerDomain in DOMAINS) {
            return true
        }
        
        // Alt domain kontrolü (örn: sub.dau01.ps5.update.playstation.net)
        for (blockedDomain in DOMAINS) {
            if (lowerDomain == blockedDomain || 
                lowerDomain.endsWith(".$blockedDomain") ||
                blockedDomain.endsWith(".$lowerDomain")) {
                return true
            }
        }
        
        return false
    }
    
    fun getFormattedList(): String {
        return DOMAINS.sorted().joinToString("\n")
    }
    
    fun getCount(): Int = DOMAINS.size
}
