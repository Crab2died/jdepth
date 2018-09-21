function timeZoneDate(timestamp, timeZone) {

    var d = new Date(timestamp);
    var zone = timeZoneId(timeZone);
    if (zone === null)
        return d;
    var localTime = d.getTime(),
        localOffset = d.getTimezoneOffset() * 60000,
        utc = localTime + localOffset,
        localSecondTime = utc + (3600000 * zone);

    return new Date(localSecondTime);
}

function timeZoneId(timeZone) {

    if (timeZone === "" || timeZone === null || timeZone === undefined)
        return null;

    var TIME_ZONE =
        [
            {"Etc/GMT+12": -12},
            {"Etc/GMT+11": -11},
            {"Pacific/Honolulu": -10},
            {"America/Anchorage": -9},
            {"America/Santa_Isabel": -8},
            {"America/Los_Angeles": -8},
            {"America/Chihuahua": -7},
            {"America/Phoenix": -7},
            {"America/Denver": -7},
            {"America/Guatemala": -6},
            {"America/Chicago": -6},
            {"America/Regina": -6},
            {"America/Mexico_City": -6},
            {"America/Bogota": -5},
            {"America/Indiana/Indianapolis": -5},
            {"America/New_York": -5},
            {"America/Caracas": -4.5},
            {"America/Halifax": -4},
            {"America/Asuncion": -4},
            {"America/La_Paz": -4},
            {"America/Cuiaba": -4},
            {"America/Santiago": -4},
            {"America/St_Johns": -3.5},
            {"America/Sao_Paulo": -3},
            {"America/Godthab": -3},
            {"America/Cayenne": -3},
            {"America/Argentina/Buenos_Aires": -3},
            {"America/Montevideo": -3},
            {"Etc/GMT+2": -2},
            {"Atlantic/Cape_Verde": -1},
            {"Atlantic/Azores": -1},
            {"Africa/Casablanca": 0},
            {"Atlantic/Reykjavik": 0},
            {"Europe/London": 0},
            {"Etc/GMT": 0},
            {"Europe/Berlin": 1},
            {"Europe/Paris": 1},
            {"Africa/Lagos": 1},
            {"Europe/Budapest": 1},
            {"Europe/Warsaw": 1},
            {"Africa/Windhoek": 1},
            {"Europe/Istanbul": 2},
            {"Europe/Kiev": 2},
            {"Africa/Cairo": 2},
            {"Asia/Damascus": 2},
            {"Asia/Amman": 2},
            {"Africa/Johannesburg": 2},
            {"Asia/Jerusalem": 2},
            {"Asia/Beirut": 2},
            {"Asia/Baghdad": 3},
            {"Europe/Minsk": 3},
            {"Asia/Riyadh": 3},
            {"Africa/Nairobi": 3},
            {"Asia/Tehran": 3},
            {"Europe/Moscow": 4},
            {"Asia/Tbilisi": 4},
            {"Asia/Yerevan": 4},
            {"Asia/Dubai": 4},
            {"Asia/Baku": 4},
            {"Indian/Mauritius": 4},
            {"Asia/Kabul": 4.5},
            {"Asia/Tashkent": 5},
            {"Asia/Karachi": 5},
            {"Asia/Colombo": 5.5},
            {"Asia/Kolkata": 5.5},
            {"Asia/Kathmandu": 5.75},
            {"Asia/Almaty": 6},
            {"Asia/Yekaterinburg": 6},
            {"Asia/Rangoon": 6.5},
            {"Asia/Bangkok": 7},
            {"Asia/Novosibirsk": 7},
            {"Asia/Krasnoyarsk": 8},
            {"Asia/Ulaanbaatar": 8},
            {"Asia/Shanghai": 8},
            {"Australia/Perth": 8},
            {"Asia/Singapore": 8},
            {"Asia/Taipei": 8},
            {"Asia/Irkutsk": 9},
            {"Asia/Seoul": 9},
            {"Asia/Tokyo": 9},
            {"Australia/Darwin": 9.5},
            {"Australia/Adelaide": 9.5},
            {"Australia/Hobart": 10},
            {"Asia/Yakutsk": 10},
            {"Australia/Brisbane": 10},
            {"Pacific/Port_Moresby": 10},
            {"Pacific/Guadalcanal": 11},
            {"Etc/GMT-12": 12},
            {"Pacific/Fiji": 12},
            {"Asia/Magadan": 12},
            {"Pacific/Auckland": 12},
            {"Pacific/Tongatapu": 13},
            {"Pacific/Apia": 13}
        ];

    for (var i = 0, l = TIME_ZONE.length; i < l; i++) {
        for (var key in TIME_ZONE[i]) {
            if (key === timeZone) {
                return TIME_ZONE[i][timeZone]
            }
        }

    }
    return null;
}


