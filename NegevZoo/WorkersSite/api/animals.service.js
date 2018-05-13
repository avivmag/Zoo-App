app.factory('animalService', ['httpService', function (httpService) {
    var serviceBaseUrl = 'animals';

    var getPreservation = function () {
        return [
            { value: 7, format: 'לא ידוע' },
            { value: 1, format: 'ללא חשש' },
            { value: 2, format: 'קרוב לאיום' },
            { value: 3, format: 'פגיע' },
            { value: 4, format: 'בסכנת הכחדה' },
            { value: 5, format:'בסכנת הכחדה חמורה' },
            { value: 6, format: 'נכחד בטבע' }
        ];
    }

    var animalService = {
        getAllAnimals:          function (language)             { return httpService.httpGet({ url: [serviceBaseUrl, 'all', language] }); },
        getAnimalById:          function (animalId, language)   { return httpService.httpGet({ url: [serviceBaseUrl, 'id', animalId, language] }); },
        getAnimalsByEnclosure:  function (encId, language)      { return httpService.httpGet({ url: [serviceBaseUrl, 'enclosure', encId] }); },
        getAnimalDetailsById:   function (animalId)             { return httpService.httpGet({ url: [serviceBaseUrl, 'details', 'all', animalId]} ) },
        getAnimalByName:        function (name, language)       { return httpService.httpGet({ url: [serviceBaseUrl, 'name', name, language] }); },
        updateAnimal:           function (animal)               { return httpService.httpPost({ url: [serviceBaseUrl, 'update'], body: animal }); },
        updateAnimalDetail:     function (animalDetail)         { return httpService.httpPost({ url: [serviceBaseUrl, 'detail', 'update'], body: animalDetail }); },
        deleteAnimal:           function (animalId)             { return httpService.httpDelete({ url: [serviceBaseUrl, 'delete', animalId] }); },
        getPreservation
    };

    return animalService;
}]);