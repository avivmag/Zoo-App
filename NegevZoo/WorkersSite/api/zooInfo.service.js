app.factory('zooInfoService', ['httpService', function (httpService) {
    var pricesBaseUrl           = 'prices';
    var openingHoursBaseUrl     = 'openingHours';
    var contactInfosBaseUrl     = 'contactInfos';
    var specialEventsBaseUrl    = 'specialEvents';
    var wallFeedBaseUrl         = 'wallFeed';
    var generalInfoBaseUrl      = 'about';
    var languagesBaseUrl        = 'languages';

    var prices          = {
        getAllPrices:           function (language)                         { return httpService.httpGet({ url: [pricesBaseUrl, 'all', language] }); },
        updatePrice:            function (price)                            { return httpService.httpPost({ url: [pricesBaseUrl, 'update'], body: price }); },
        deletePrice:            function (priceId)                          { return httpService.httpDelete({ url: [pricesBaseUrl, 'delete', priceId] }); }
    };

    var getAllLanguages =       function ()                                 { return httpService.httpGet({ url: [languagesBaseUrl, 'all']}); };

    var openingHours    = {
        getAllOpeningHours:     function (language)                         { return httpService.httpGet({ url: [openingHoursBaseUrl, 'all', language] }); },
        updateOpeningHour:      function (openingHour)                      { return httpService.httpPost({ url: [openingHoursBaseUrl, 'update'], body: openingHour }); },
        deleteOpeningHour:      function (openingHourId)                    { return httpService.httpDelete({ url: [openingHoursBaseUrl, 'delete', openingHourId] }); }
    };

    var contactInfo     = {
        getAllContactInfos:     function (language)                         { return httpService.httpGet({ url: [contactInfosBaseUrl, 'all', language] }); },
        updateContactInfo:      function (contactInfo)                      { return httpService.httpPost({ url: [contactInfosBaseUrl, 'update'], body: contactInfo }); },
        deleteContactInfo:      function (contactInfoId)                    { return httpService.httpDelete({ url: [contactInfosBaseUrl, 'delete', contactInfoId] }); }
    };
    var specialEvents   = {
        getAllSpecialEvents:    function (language)                         { return httpService.httpGet({ url: [specialEventsBaseUrl, 'all', language] }); },
        getSpecialEventByDate:  function (startDate, endDate, language)     { return httpService.httpGet({ url: [specialEventsBaseUrl, 'date', startDate, endDate, language] }); },
        updateSpecialEvent:     function (specialEvent, isPush)             { return httpService.httpPost({ url: [specialEventsBaseUrl, 'update', isPush], body: specialEvent }); },
        deleteSpecialEvent:     function (specialEventId)                   { return httpService.httpDelete({ url: [specialEventsBaseUrl, 'delete', specialEventId] }); }
    };
    var feedWall        = {
        getAllFeeds:            function (language)                         { return httpService.httpGet({ url: [wallFeedBaseUrl, 'all', language] }); },
        updateFeed:             function (wallFeed, isPush, isWallFeed)     { return httpService.httpPost({ url: [wallFeedBaseUrl, 'update', isPush, isWallFeed], body: wallFeed }); },
        deleteFeed:             function (wallFeedId)                       { return httpService.httpDelete({ url: [wallFeedBaseUrl, 'delete', wallFeedId] }); }
    };
    var generalInfo     = {
        getAboutInfo:           function (language)                         { return httpService.httpGet({ url: [generalInfoBaseUrl, 'all', language] }); },
        updateAboutInfo:        function (aboutInfo)                        { return httpService.httpPost({ url: [generalInfoBaseUrl, 'update'], body: aboutInfo }); },
        getOpeningHourNote:     function (language)                         { return httpService.httpGet({ url: [generalInfoBaseUrl, 'all', language] }); },
        updateOpeningHourNote:  function (openingHourNote)                  { return httpService.httpPost({ url: [generalInfoBaseUrl, 'update'], body: openingHourNote }); },
        getContactInfoNote:     function (language)                         { return httpService.httpGet({ url: [generalInfoBaseUrl, 'all', language] }); },
        updateContactInfoNote:  function (contactInfoNote)                  { return httpService.httpPost({ url: [generalInfoBaseUrl, 'update'], body: contactInfoNote }); },
    };

    var zooInfoService = {
        prices,
        openingHours,
        contactInfo,
        specialEvents,
        feedWall,
        generalInfo,
        getAllLanguages
    }
    
    return zooInfoService;
}]);