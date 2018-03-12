app.factory('enclosureService', ['$http', function ($http) {
    var baseURL = app.baseURL + 'enclosures/';

    var httpGet = function (httpRequest) {
        httpRequest.url = baseURL + httpRequest.url;

        return ($http({
            url:        httpRequest.url,
            method:     'GET'
        }));
    }

    var httpPost = function (httpRequest) {
        httpRequest.url = baseURL + httpRequest.url;

        return ($http({
            url:        httpRequest.url,
            method:     'POST',
            data:       httpRequest.body
        }));
    }

    var httpDelete = function (httpRequest) {
        httpRequest.url = baseURL + httpRequest.url;

        return ($http({
            url:    httpRequest.url,
            method: 'DELETE',
            data:   httpRequest.body
        }));
    }

    var enclosureService = {
        getAllEnclosures:       function (language)                         { return httpGet({ url: 'all/' + language }); },
        getEnclosureById:       function (encId, language)                  { return httpGet({ url: 'id/' + encId + '/' + language }); },
        getEnclosureByName:     function (name, language)                   { return httpGet({ url: 'name/' + name + '/' + language }); },
        getEnclosureByPosition: function (longtitude, latitude, language)   { return httpGet({ url: 'position/' + longtitude + '/' + latitude + '/' + language }); },
        getRecurringEvents:     function (encId, language)                  { return httpGet({ url: 'recurring/' + encId + '/' + language }); },
        updateEnclosure:        function (enclosure)                        { return httpPost({ url: 'update', body: enclosure }); },
        deleteEnclosure:        function (encId)                            { return httpDelete({ url: 'delete/' + encId }); }
    };

    return enclosureService;
}]);