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