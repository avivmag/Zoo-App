app.factory('animalService', ['httpService', function (httpService) {
    var serviceBaseUrl = 'animals';

    var animalService = {
        getAllAnimals:          function (language)             { return httpService.httpGet({ url: [serviceBaseUrl, 'all', language] }); },
        getAnimalById:          function (animalId, language)   { return httpService.httpGet({ url: [serviceBaseUrl, 'id', animalId, language] }); },
        getAnimalsByEnclosure:  function (encId, language)      { return httpService.httpGet({ url: [serviceBaseUrl, 'enclosure', encId, language] }); },
        getAnimalByName:        function (name, language)       { return httpService.httpGet({ url: [serviceBaseUrl, 'name', name, language] }); },
        updateAnimal:           function (animal)               { return httpService.httpPost({ url: [serviceBaseUrl, 'update'], body: animal }); },
        deleteAnimal:           function (animalId)             { return httpService.httpDelete({ url: [serviceBaseUrl, 'delete', animalId] }); }
    };

    return animalService;
}]);