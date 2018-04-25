app.controller('indexController', 
    ['$q',
     '$scope',
     '$mdDialog',
     '$state',
     'utilitiesService',
     'enclosureService',
     'animalService',
     'zooInfoService',

    function indexController($q, $scope, $mdDialog, $state, utilitiesService, enclosureService, animalService, zooInfoService) 
    {
        app.getLanguages = function () {
            // If languages have already been fetched, return a promise of them.
            if (angular.isDefined(app.languages)) {
                return $q((resolve, reject) => {
                    resolve({ data: app.languages });
                });
            }
            // Otherwise, fetch them and return the promise of the result.
            else {
                var langsQuery = zooInfoService.getAllLanguages();

                langsQuery.then(
                    function (data) {
                        app.languages           = data.data;
                        app.defaultLanguage     = app.languages.find(lang => lang.name === 'עברית');
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                        return $q((resolve, reject) => {
                            reject();
                        });
                    });

                return langsQuery;
            }
        }

        $state.go('login');
    }]);
        //var testEnclosure = {

        //    Id:             0,
        //    Name:           'test55',
        //    Story:          'this is my story',
        //    Language:       2,
        //    longtitude:     35.0,
        //    latitude:       30.0
        //};

        //var testAnimal = {

        //    Id:         0,
        //    Name:       'testAnimal55',
        //    Story:      'this is my story',
        //    EncId:      1,
        //    Language:   2,
        //};

        //var allEnclosuresQuery              = enclosureService.getAllEnclosures(1).then(function (data) { console.log('allEnclosures', data); }, function (error) { console.log('allFail', error); });
        //var byNameEnclosuresQuery           = enclosureService.getEnclosureByName('תצוגה', 1).then(function (data) { console.log('byName(b)', data); }, function (error) { console.log('byNameFail', error); });
        //var recurringEventsQuery            = enclosureService.getRecurringEvents(2, 1).then(function (data) { console.log('recurring', data); }, function (error) { console.log('recurringFail', error); })
        //var emptyUpdateEnclosure            = enclosureService.updateEnclosure().then(function (data) { console.log('empty', data); }, function (fail) { console.log('emptyFail', fail); });
        //var updateEnclosureQuery            = enclosureService.updateEnclosure(testEnclosure).then(function (data) { console.log('updated', data); }, function (fail) { console.log('updatedFail', fail); });
        //var deleteEnclosureQuery            = enclosureService.deleteEnclosure(2).then(function (data) { console.log('deleted', data); }, function (fail) { console.log('deleteFail', fail); });
        //var getEnclosureByIdQuery           = enclosureService.getEnclosureById(1, 2).then(function (data) { console.log('gotById', data); }, function (fail) { console.log('gotByIdFail', fail); });
        //var getEnclosureByPositionQuery     = enclosureService.getEnclosureByPosition(400, 400, 2).then(function (data) { console.log('gotByPos', data); }, function (fail) { console.log('gotByPosFail', fail); });

        //var allAnimalsQuery                 = animalService.getAllAnimals(2).then(function (data) { console.log('allAnimals', data); }, function (error) { console.log('allAnimalsFail', error); });
        //var getAnimalByIdQuery              = animalService.getAnimalById(1, 2).then(function (data) { console.log('gotAnimalById', data); }, function (fail) { console.log('gotAnimalByIdFail', fail); });
        //var getAnimalByEnclosureQuery       = animalService.getAnimalsByEnclosure(1, 2).then(function (data) { console.log('gotAnimalByEnc', data); }, function (fail) { console.log('gotAnimalByEncFail', fail); });
        //var getAnimalByNameQuery            = animalService.getAnimalByName('Mon', 2).then(function (data) { console.log('gotAnimalByName', data); }, function (fail) { console.log('gotAnimalByNameFail', fail); });
        //var updateEmptyAnimalQuery          = animalService.updateAnimal().then(function (data) { console.log('updateEmptyAnimal', data); }, function (fail) { console.log('updateEmptyAnimalFail', fail); });
        //var updateAnimalQuery               = animalService.updateAnimal(testAnimal).then(function (data) { console.log('updateAnimal', data); }, function (fail) { console.log('updateAnimalFail', fail); });
        //var deleteAnimalQuery               = animalService.deleteAnimal(2).then(function (data) { console.log('deleteAnimal', data); }, function (fail) { console.log('deleteAnimalFail', fail); });
