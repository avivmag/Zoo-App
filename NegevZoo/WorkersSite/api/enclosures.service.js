app.factory('enclosureService', ['$http', function ($http) {
    var baseURL = app.baseURL + 'enclosures/';

    var httpGet = function (httpRequest, success, fail) {
        httpRequest.url = baseURL + httpRequest.url;

        $http({
            url: 'http://negevzoo.sytes.net:50000/animals/1',
            method: 'GET',
            headers: {
                'Content-Type': 'application/json; charset=utf-8',
                'Access-Control-Allow-Origin': '*'
            }
        }).then(function () { console.log('hi'); });
    }

    var enclosureService = {
        getAllEnclosures: function (language) { return httpGet({ url: language }) }
    };

    return enclosureService;
}]);