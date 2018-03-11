app.controller('indexController', 
	['$scope', 
     '$state', 
     'enclosureService',

     function indexController($scope, $state, enclosureService) 
        {
            var vm = this;
            var testEnclosure = {
                Id:             0,
                Name:           'test55',
                Story:          'this is my story',
                Language:       2,
                longtitude:     35.0,
                latitude:       30.0
            };


            allEnclosuresQuery      = enclosureService.getAllEnclosures(1).then(function (data) { console.log('all', data); }, function (error) { console.log('allFail', error); });
            byNameEnclosuresQuery   = enclosureService.getEnclosureByName('תצוגה', 1).then(function (data) { console.log('byName(b)', data); }, function (error) { console.log('byNameFail', error); });
            recurringEventsQuery    = enclosureService.getRecurringEvents(2, 1).then(function (data) { console.log('recurring', data); }, function (error) { console.log('recurringFail', error); })
            emptyUpdateEnclosure    = enclosureService.updateEnclosure().then(function (data) { console.log('empty', data); }, function (fail) { console.log('emptyFail', fail); });
            updateEnclosureQuery    = enclosureService.updateEnclosure(testEnclosure).then(function (data) { console.log('updated', data); }, function (fail) { console.log('updatedFail', fail); });
            deleteEnclosureQuery    = enclosureService.deleteEnclosure(2).then(function (data) { console.log('deleted', data); }, function (fail) { console.log('deleteFail', fail); });
            getEnclosureByIdQuery   = enclosureService.getEnclosureById(1, 2).then(function (data) { console.log('gotById', data); }, function (fail) { console.log('gotByIdFail', fail); });
            getEnclosureByPositionQuery = enclosureService.getEnclosureByPosition(400, 400, 2).then(function (data) { console.log('gotByPos', data); }, function (fail) { console.log('gotByPosFail', fail); });

            $state.go('login');

}]);