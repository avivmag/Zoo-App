﻿app.factory('enclosureService', ['httpService', function (httpService) {
    var serviceBaseUrl = 'enclosures';

    var enclosureDetails   = {
        getAllEnclosures:           function (language)                         { return httpService.httpGet({ url: [serviceBaseUrl, 'all', language] }); },
        getEnclosureDetailsById:    function (encId)                            { return httpService.httpGet({ url: [serviceBaseUrl, 'details', 'all', encId] }); },
        getRecurringEvents:         function (encId, language)                  { return httpService.httpGet({ url: [serviceBaseUrl, 'recurring', encId, language] }); },
        updateRecurringEvent:       function (recurringEvent)                   { return httpService.httpPost({ url: [serviceBaseUrl, 'recurring', 'update'], body: recurringEvent }); },
        updateEnclosureDetail:      function (enclosureDetail)                  { return httpService.httpPost({ url: [serviceBaseUrl, 'detail', 'update'], body: enclosureDetail }); },
        deleteEnclosure:            function (encId)                            { return httpService.httpDelete({ url: [serviceBaseUrl, 'delete', encId] }); },
        deleteRecurringEvent:       function (encId, recurringEventId)          { return httpService.httpDelete({ url: [serviceBaseUrl, encId, 'recurring', 'delete', recurringEventId] }); }
    };

    var enclosures          = {
        getAllEnclosures:           function ()                                     { return httpService.httpGet({url: [serviceBaseUrl, 'types', 'all']}) },
        updateEnclosure:            function (enclosure)                            { return httpService.httpPost({ url: [serviceBaseUrl, 'update'], body: enclosure }); },
        deleteEnclosure:            function (encId)                                { return httpService.httpDelete({ url: [serviceBaseUrl, 'delete', encId] }); },
        deleteEnclosurePicture:     function (encId, pictureId)                     { return httpService.httpDelete({ url: [serviceBaseUrl, encId, 'picture', pictureId, 'delete']}); },
        deleteEnclosureVideo:       function (encId, videoId)                       { return httpService.httpDelete({ url: [serviceBaseUrl, encId, 'video', videoId, 'delete']}); },
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