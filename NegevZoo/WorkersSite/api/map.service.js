app.factory('mapService', ['httpService', function (httpService) {
    var serviceBaseUrl  = 'map';

    var mapService      = {
        getMap:         function ()             { return httpService.httpGet({ url: [serviceBaseUrl, 'url'] }); },
        getAllMarkers:  function ()             { return httpService.httpGet({ url: [serviceBaseUrl, 'markers'] }); }
    };

    return mapService;
}]);