app.controller('zooAnimalCtrl', ['$q', '$state', '$scope', '$stateParams', 'utilitiesService', 'animalService', 'fileUpload',

    function animalController($q, $state, $scope, $stateParams, utilitiesService, animalService, fileUpload) {
        if (!angular.isDefined($stateParams.enclosure)) {
            $state.go('mainMenu.enclosures.list');
        }

        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];
                
                initializeComponent();
                $scope.updateAnimals();
            });

        function initializeComponent() {
            $scope.isLoading        = true;

            if (angular.isDefined($stateParams.animal)) {
                $scope.isEdit       = true;
            }

            $scope.enclosure        = $stateParams.enclosure;
            $scope.baseURL          = app.baseURL;
            $scope.animal           = $stateParams.animal || { enclosureId: $scope.enclosure.id };
            $scope.preservation     = animalService.getPreservation();

            $scope.updateAnimals    = function () {
                if ($scope.isEdit) {
                    $scope.isLoading                = true;

                    animalService.getAnimalDetailsById($scope.animal.id).then(
                        function (data) {
                            $scope.animalDetails    = data.data;
    
                            for (let i = 1; i <= 4; i++) {
                                if (!$scope.animalDetails.some(ed => ed.language === i)) {
                                    $scope.animalDetails.push({ animalId: $scope.animal.id, language: i });
                                }
                            }
    
                            $scope.animalDetails.sort(function(a, b) { return a.language-b.language; });
    
                            $scope.isLoading    = false;
                        },
                        function () {
                            utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
    
                            $scope.isLoading    = false;
                        });
                }
                else {
                    $scope.isLoading            = false;
                }
            };

            $scope.addAnimal        = function(animal) {
                if (!checkAnimal(animal)) {
                    return;
                }

                $scope.isLoading            = true;
                    var successContent      = !$scope.isEdit ? 'החיה נוספה בהצלחה!' : 'החיה עודכנה בהצלחה!';
                    var failContent         = !$scope.isEdit ? 'התרחשה שגיאה בעת שמירת החיה' : 'התרחשה שגיאה בעת עדכון החיה';

                    var pictureUploadQuery  = uploadProfilePicture($scope.profilePic, animal);

                    var uploadPromises      = [pictureUploadQuery];

                    $q.all(uploadPromises).then(
                        () => {
                            animalService.updateAnimal(animal).then(
                                function (response) {
                                    utilitiesService.utilities.alert(successContent);
                                    
                                    $scope.isLoading        = false;

                                    $stateParams.animal     = response.data;
                                    $stateParams.enclosure  = $scope.enclosure;

                                    initializeComponent();
                                    $scope.updateAnimals();
                                },
                                function () {
                                    utilitiesService.utilities.alert(failContent);
        
                                    $scope.isLoading = false;
                                });
                        },
                        () => {
                            utilitiesService.utilities.alert(failContent);

                            $isLoading = false;
                        });
            };

            $scope.deleteAnimal     = function(animalId) {
                $scope.isLoading                    = true;

                animalService.deleteAnimal(animalId).then(
                    function() {
                        utilitiesService.utilities.alert("החיה נמחקה בהצלחה!");

                        $state.go('mainMenu.enclosures.enclosure', { enclosure: $scope.enclosure });
                    },

                    function () {
                        utilitiesService.utilities.alert("חלה שגיאה במחיקת החיה.");

                        $scope.isLoading            = false;
                    });
            }

            $scope.addAnimalDetail       = function(animalDetail) {
                $scope.isLoading            = true;
                    var successContent      = $scope.page === 'create' ? 'החיה נוספה בהצלחה!' : 'החיה עודכנה בהצלחה!';
                    var failContent         = $scope.page === 'create' ? 'התרחשה שגיאה בעת שמירת החיה' : 'התרחשה שגיאה בעת עדכון החיה';

                    var audioUploadQuery    = uploadAudioFile(animalDetail);

                    $q.all([audioUploadQuery]).then(
                        () => {
                            animalService.updateAnimalDetail(animalDetail).then(
                                function () {
                                    utilitiesService.utilities.alert(successContent);

                                    $scope.isLoading = false;
                                },
                                function () {
                                    utilitiesService.utilities.alert(failContent);

                                    $scope.isLoading = false;
                                });
                        },
                        () => utilitiesService.utilities.alert(failContent));
            }

            $scope.playSound                = function(audioFile) {
                if (!$scope.audio) {
                    $scope.audio = new Audio($scope.baseURL + audioFile);
                }
                
                if ($scope.audio.paused) {
                    $scope.audio.play();
                }
                else {
                    $scope.audio.pause();
                    $scope.audio.currentTime    = 0;
                }
            };
        };

        function checkAnimal(animal) {
            if (!animal) {
                return false;
            }

            if (!angular.isDefined(animal.name) || animal.name == '') {
                utilitiesService.utilities.alert('אנא בחר שם לחיה');

                return false;
            }

            if (!angular.isDefined(animal.preservation)) {
                utilitiesService.utilities.alert('אנא בחר רמת שימור לחיה');

                return false;
            }

            return true;
        }

        function uploadAudioFile(animalDetail) {
            if (!angular.isDefined(animalDetail.animalAudioFile) || animalDetail.animalAudioFile === null) {
                return;
            }

            $scope.isLoading        = true;

            var uploadUrl           = 'animals/upload/audio/false';

            var fileUploadQuery     = fileUpload.uploadFileToUrl(animalDetail.animalAudioFile, uploadUrl).then(
                (success)   => {
                    animalDetail.audioUrl    = success.data[0];
                },
                ()          => {
                    utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');
                });

            return fileUploadQuery;
        }

        function uploadProfilePicture (picture, animal) {
            if (!angular.isDefined(picture)) {
                return;
            }

            $scope.isLoading        = true;

            var uploadUrl           = 'animals/upload/pictures';

            var fileUploadQuery     = fileUpload.uploadFileToUrl(picture, uploadUrl).then(
                (success)   => {
                    animal.pictureUrl    = success.data[0];

                    $scope.profilePic       = null;
                },
                ()          => {
                    utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');
                });

            return fileUploadQuery;
        };
}]);