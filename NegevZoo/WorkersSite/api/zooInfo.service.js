app.factory('zooInfoService', ['httpService', function (httpService) {
    var pricesBaseUrl           = 'prices';
    var openingHoursBaseUrl     = 'openingHours';
    var contactInfosBaseUrl     = 'contactInfos';
    var specialEventsBaseUrl    = 'specialEvents';
    var wallFeedBaseUrl         = 'wallFeed';
    var aboutInfoBaseUrl        = 'about';
    var languagesBaseUrl        = 'languages';

    var prices          = {
        getAllPrices:           function (language)                         { return httpService.httpGet({ url: [pricesBaseUrl, 'all', language] }); },
        updatePrice:            function (price)                            { return httpService.httpPost({ url: [pricesBaseUrl, 'update'], body: price }); },
        deletePrice:            function (priceId)                          { return httpService.httpDelete({ url: [pricesBaseUrl, 'delete', priceId] }); }
    };

    var getAllLanguages =       function ()                                 { return httpService.httpGet({ url: [languagesBaseUrl, 'all']}); };

    var openingHours    = {
        getAllOpeningHours:     function ()                                 { return httpService.httpGet({ url: [openingHoursBaseUrl, 'type', 'all'] }); },
        updateOpeningHour:      function (openingHour)                      { return httpService.httpPost({ url: [openingHoursBaseUrl, 'update'], body: openingHour }); },
        deleteOpeningHour:      function (openingHourId)                    { return httpService.httpDelete({ url: [openingHoursBaseUrl, 'delete', openingHourId] }); },
        getOpeningHourNote:     function (language)                         { return httpService.httpGet({ url: [openingHoursBaseUrl, 'openingHourNote', language] }); },
        updateOpeningHourNote:  function (openingHourNote, language)        { return httpService.httpPost({ url: [openingHoursBaseUrl, 'update', language], body: openingHourNote }); }
    };

    var contactInfo     = {
        getAllContactInfos:     function (language)                         { return httpService.httpGet({ url: [contactInfosBaseUrl, 'all', language] }); },
        updateContactInfo:      function (contactInfo)                      { return httpService.httpPost({ url: [contactInfosBaseUrl, 'update'], body: contactInfo }); },
        deleteContactInfo:      function (contactInfoId)                    { return httpService.httpDelete({ url: [contactInfosBaseUrl, 'delete', contactInfoId] }); },
        getContactInfoNote:     function (language)                         { return httpService.httpGet({ url: [contactInfosBaseUrl, 'contactInfoNote', language] }); },
        updateContactInfoNote:  function (contactInfoNote, language)        { return httpService.httpPost({ url: [contactInfosBaseUrl, 'update', language], body: contactInfoNote }); }
    };
    var specialEvents   = {
        getAllSpecialEvents:    function (language)                         { return httpService.httpGet({ url: [specialEventsBaseUrl, 'all', language] }); },
        updateSpecialEvent:     function (specialEvent, isPush)             { return httpService.httpPost({ url: [specialEventsBaseUrl, 'update', isPush], body: specialEvent }); },
        deleteSpecialEvent:     function (specialEventId)                   { return httpService.httpDelete({ url: [specialEventsBaseUrl, 'delete', specialEventId] }); }
    };
    var feedWall        = {
        getAllFeeds:            function (language)                         { return httpService.httpGet({ url: [wallFeedBaseUrl, 'all', language] }); },
        updateFeed:             function (wallFeed)                         { return httpService.httpPost({ url: [wallFeedBaseUrl, 'update', wallFeed.isPushMessage, wallFeed.isFeedWall, wallFeed.pushRecipients], body: wallFeed }); },
        deleteFeed:             function (wallFeedId)                       { return httpService.httpDelete({ url: [wallFeedBaseUrl, 'delete', wallFeedId] }); }
    };
    var aboutInfo     = {
        getAboutInfo:           function (language)                         { return httpService.httpGet({ url: [aboutInfoBaseUrl, 'info', language] }); },
        updateAboutInfo:        function (aboutInfo, language)              { return httpService.httpPost({ url: [aboutInfoBaseUrl, 'update', language], body: aboutInfo }); }
    };

    var zooInfoService = {
        prices,
        openingHours,
        contactInfo,
        specialEvents,
        feedWall,
        aboutInfo,
        getAllLanguages
    }
    
    return zooInfoService;
}]);