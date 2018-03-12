app.factory('httpService', ['$http', function ($http) {
    var baseURL = app.baseURL;

    function getUrl(baseURL, urlArray) {
        return baseURL + urlArray.join('/');
    };

    var httpGet = function (httpRequest) {
        httpRequest.url = getUrl(baseURL, httpRequest.url);

        return ($http({
            url:        httpRequest.url,
            method:     'GET'
        }));
    };

    var httpPost = function (httpRequest) {
        httpRequest.url = getUrl(baseURL, httpRequest.url);

        return ($http({
            url:        httpRequest.url,
            method:     'POST',
            data:       httpRequest.body
        }));
    };

    var httpDelete = function (httpRequest) {
        httpRequest.url = getUrl(baseURL, httpRequest.url);

        return ($http({
            url:    httpRequest.url,
            method: 'DELETE',
            data:   httpRequest.body
        }));
    };

    var httpService = {
        httpGet,
        httpPost,
        httpDelete
    };

    return httpService;
}]);