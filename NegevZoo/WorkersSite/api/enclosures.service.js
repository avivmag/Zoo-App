app.factory('enclosureService', ['$http', function ($http) {
    var baseURL = app.baseURL + 'enclosures/';

    var config = {
        headers: { 'Access-Control-Allow-Origin': '*' }
    };

    var httpGet = function (httpRequest, success, fail) {
        httpRequest.url = baseURL + httpRequest.url;


        $http.get('http://negevzoo.sytes.net:50000/animals/1').then(function (data) { console.log(data) });

        //return $http.get(httpRequest.url);
    }

    var enclosureService = {
        getAllEnclosures: function (language) { return httpGet({ url: language }) }
    };

    return enclosureService;
}]);