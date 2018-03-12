app.factory('zooInfoService', ['httpService', function (httpService) {
    var pricesBaseUrl           = 'prices';
    var openingHoursBaseUrl     = 'openingHours';
    var contactInfosBaseUrl     = 'contactInfos';
    var specialEventsBaseUrl    = 'specialEvents';
    var wallFeedBaseUrl         = 'wallFeed';
    var generalInfoBaseUrl      = 'about';

    var prices          = {
        getAllPrices:           function (language)         { return httpService.httpGet({ url: [servicebaseUrl, 'all', language] }); },
        updatePrice:            function (price)            { return httpService.httpPost({ url: [servicebaseUrl, 'update'], body: price }); },
        deletePrice:            function (priceId)          { return httpService.httpDelete({ url: [servicebaseUrl, 'delete', priceId] }); }
    };

    var openingHours    = {
        getAllOpeningHours:     function (language)         { return httpService.httpGet({ url: [servicebaseUrl, 'all', language] }); },
        updateOpeningHour:      function (openingHour)      { return httpService.httpPost({ url: [servicebaseUrl, 'update'], body: openingHour }); },
        deleteOpeningHour:      function (openingHourId)    { return httpService.httpDelete({ url: [servicebaseUrl, 'delete', openingHourId] }); }
    };

    var contactInfo     = {
        getAllContactInfos:     function (language)         { return httpService.httpGet({ url: [servicebaseUrl, 'all', language] }); },
        updateContactInfo:      function (contactInfo)      { return httpService.httpPost({ url: [servicebaseUrl, 'update'], body: contactInfo }); },
        deleteContactInfo:      function (contactInfoId)    { return httpService.httpDelete({ url: [servicebaseUrl, 'delete', contactInfoId] }); }
    };
    var specialEvents   = {
        getAllSpecialEvents:    function (language)                         { return httpService.httpGet({ url: [servicebaseUrl, 'all', language] }); },
        getSpecialEventByDate:  function (startDate, endDate, language)     { return httpService.httpGet({ url: [servicebaseUrl, 'date', startDate, endDate, language] }); },
        updateSpecialEvent:     function (specialEvent)                     { return httpService.httpPost({ url: [servicebaseUrl, 'update'], body: specialEvent }); },
        deleteSpecialEvent:     function (specialEventId)                   { return httpService.httpDelete({ url: [servicebaseUrl, 'delete', specialEvent] }); }
    };
    var wallFeed        = {
        getAllWallFeeds:        function (language)     { return httpService.httpGet({ url: [servicebaseUrl, 'all', language] }); },
        updateWallFeed:         function (wallFeed)     { return httpService.httpPost({ url: [servicebaseUrl, 'update'], body: wallFeed }); },
        deleteWallFeed:         function (wallFeedId)   { return httpService.httpDelete({ url: [servicebaseUrl, 'delete', wallFeedId] }); }
    };
    var generalInfo     = {
        getAboutInfo:           function (language) { return httpService.httpGet({ url: [servicebaseUrl, 'all', language] }); },
        updateAboutInfo:        function (aboutInfo) { return httpService.httpPost({ url: [servicebaseUrl, 'update'], body: aboutInfo }); },
        getOpeningHourNote:     function (language) { return httpService.httpGet({ url: [servicebaseUrl, 'all', language] }); },
        updateOpeningHourNote:  function (openingHourNote) { return httpService.httpPost({ url: [servicebaseUrl, 'update'], body: openingHourNote }); },
        getContactInfoNote:     function (language) { return httpService.httpGet({ url: [servicebaseUrl, 'all', language] }); },
        updateContactInfoNote:  function (contactInfoNote) { return httpService.httpPost({ url: [servicebaseUrl, 'update'], body: contactInfoNote }); },

    };

    var zooInfoService = {
        prices,
        openingHours,
        contactInfo,
        specialEvents,
        wallFeed,
        generalInfo
    }
    
    return zooInfoService;
}]);