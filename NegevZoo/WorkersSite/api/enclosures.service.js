app.factory('enclosureService', ['httpService', function (httpService) {
    var serviceBaseUrl = 'enclosures';

    var enclosureDetails   = {
        getAllEnclosures:       function (language)                         { return httpService.httpGet({ url: [serviceBaseUrl, 'all', language] }); },
        getEnclosureById:       function (encId, language)                  { return httpService.httpGet({ url: [serviceBaseUrl, 'id', encId, language] }); },
        getEnclosureByName:     function (name, language)                   { return httpService.httpGet({ url: [serviceBaseUrl, 'name', name, language] }); },
        getEnclosureByPosition: function (longtitude, latitude, language)   { return httpService.httpGet({ url: [serviceBaseUrl, 'position', longtitude, latitude, language] }); },
        getRecurringEvents:     function (encId, language)                  { return httpService.httpGet({ url: [serviceBaseUrl, 'recurring', encId, language] }); },
        updateEnclosure:        function (enclosure)                        { return httpService.httpPost({ url: [serviceBaseUrl, 'update'], body: enclosure }); },
        deleteEnclosure:        function (encId)                            { return httpService.httpDelete({ url: [serviceBaseUrl, 'delete', encId] }); }
    };

    var enclosures          = {
        getAllEnclosures:       function ()                                 { return httpService.httpGet({url: [serviceBaseUrl, 'types', 'all']}) }
    };
    var enclosureService = {
        enclosures,
        enclosureDetails
    };

    return enclosureService;
}]);