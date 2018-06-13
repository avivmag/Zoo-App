app.factory('mapService', ['httpService', function (httpService) {
    var serviceBaseUrl  = 'map';

    var mapService      = {
        getMap:             function ()         { return httpService.httpGet({ url: [serviceBaseUrl, 'url'] }); },
        getAllMarkers:      function ()         { return httpService.httpGet({ url: [serviceBaseUrl, 'markers'] }); },
        getMiscMarkers:     function ()         { return httpService.httpGet({ url: [serviceBaseUrl, 'markers', 'misc'] }); },
        updateMiscMarker:   function (marker)   { return httpService.httpPost({ url: [serviceBaseUrl, 'markers', 'misc', 'update'], body: marker }); },
        deleteMiscMarker:   function (markerId) { return httpService.httpDelete({ url: [serviceBaseUrl, 'markers', markerId, 'delete'] }); },    
        uploadCoords:       function(mapCoords) { return httpService.httpPost({ url: [serviceBaseUrl, 'coords', 'upload'], body: mapCoords }); }
    };

    return mapService;
}]);