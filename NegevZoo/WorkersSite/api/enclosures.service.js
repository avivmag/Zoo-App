app.factory('enclosureService', ['httpService', function (httpService) {
    var serviceBaseUrl = 'enclosures';

    var enclosureDetails   = {
        getAllEnclosures:           function (language)                         { return httpService.httpGet({ url: [serviceBaseUrl, 'all', language] }); },
        getEnclosureDetailsById:    function (encId)                            { return httpService.httpGet({ url: [serviceBaseUrl, 'details', 'all', encId] }); },
        getEnclosureByName:         function (name, language)                   { return httpService.httpGet({ url: [serviceBaseUrl, 'name', name, language] }); },
        getEnclosureByPosition:     function (longtitude, latitude, language)   { return httpService.httpGet({ url: [serviceBaseUrl, 'position', longtitude, latitude, language] }); },
        getRecurringEvents:         function (encId, language)                  { return httpService.httpGet({ url: [serviceBaseUrl, 'recurring', encId, language] }); },
        updateEnclosureDetail:      function (enclosureDetail)                  { return httpService.httpPost({ url: [serviceBaseUrl, 'detail', 'update'], body: enclosureDetail }); },
        deleteEnclosure:            function (encId)                            { return httpService.httpDelete({ url: [serviceBaseUrl, 'delete', encId] }); }
    };

    var enclosures          = {
        getAllEnclosures:           function ()                                     { return httpService.httpGet({url: [serviceBaseUrl, 'types', 'all']}) },
        updateEnclosure:            function (enclosure)                            { return httpService.httpPost({ url: [serviceBaseUrl, 'update'], body: enclosure }); },
        deleteEnclosure:            function (encId)                                { return httpService.httpDelete({ url: [serviceBaseUrl, 'delete', encId] }); },
        getEnclosureVideosById:     function (encId)                                { return httpService.httpGet({ url: [serviceBaseUrl, 'videos', encId] }); },
        getEnclosurePicturesById:   function (encId)                                { return httpService.httpGet({ url: [serviceBaseUrl, 'pictures', encId] }); },
        updateVideoById:            function (video)                                { return httpService.httpPost({ url: [serviceBaseUrl, 'video', 'update'], body: video }); }
    };
    var enclosureService = {
        enclosures,
        enclosureDetails
    };

    return enclosureService;
}]);