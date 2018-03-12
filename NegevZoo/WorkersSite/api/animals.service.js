app.factory('animalService', ['httpService', function (httpService) {
    var servicebaseUrl = 'animals';

    var animalService = {
        getAllAnimals:          function (language)             { return httpService.httpGet({ url: [servicebaseUrl, 'all', language] }); },
        getAnimalById:          function (animalId, language)   { return httpService.httpGet({ url: [servicebaseUrl, 'id', animalId, language] }); },
        getAnimalsByEnclosure:  function (encId, language)      { return httpService.httpGet({ url: [servicebaseUrl, 'enclosure', encId, language] }); },
        getAnimalByName:        function (name, language)       { return httpService.httpGet({ url: [servicebaseUrl, 'name', name, language] }); },
        updateAnimal:           function (enclosure)            { return httpService.httpPost({ url: [servicebaseUrl, 'update'], body: animal }); },
        deleteAnimal:           function (animalId)             { return httpService.httpDelete({ url: [servicebaseUrl, 'delete', animalId] }); }
    };

    return animalService;
}]);