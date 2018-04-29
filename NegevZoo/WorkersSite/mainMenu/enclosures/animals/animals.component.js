app.controller('zooAnimalsCtrl', ['$q', '$state', '$scope', '$stateParams', 'utilitiesService', 'animalService', 'fileUpload',

    function enclosureController($q, $state, $scope, $stateParams, utilitiesService, animalService, fileUpload) {
        if (!angular.isDefined($stateParams.enclosureId)) {
            $state.go('mainMenu');
        }

        $scope.isLoading            = true;

        initializeComponent();

        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];

                $scope.updateAnimals();
            });

        function initializeComponent() {
            if (angular.isDefined($stateParams.animal)) {
                $scope.isEdit       = true;
            }

            $scope.enclosureId      = $stateParams.enclosureId;
            $scope.baseURL          = app.baseURL;
            $scope.animal           = $stateParams.animal || { enclosureId: $scope.enclosureId };

            $scope.updateAnimals    = function () {
                if ($scope.isEdit) {
                    $scope.isLoading        = true;

                    animalService.getAnimalDetailsById($scope.animal.id).then(
                        function (data) {
                            $scope.animalDetails = data.data;
    
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
                $scope.isLoading            = true;
                    var successContent      = $scope.page === 'create' ? 'החיה נוספה בהצלחה!' : 'החיה עודכנה בהצלחה!';
                    var failContent         = $scope.page === 'create' ? 'התרחשה שגיאה בעת שמירת החיה' : 'התרחשה שגיאה בעת עדכון החיה';

                    var pictureUploadQuery  = uploadProfilePicture($scope.profilePic, animal);

                    var uploadPromises      = [pictureUploadQuery];

                    $q.all(uploadPromises).then(
                        () => {
                            animalService.updateAnimal(animal).then(
                                function () {
                                    utilitiesService.utilities.alert(successContent);
                                    
                                    // TODO:: Think about where to redirect after animal save.
                                    $scope.isLoading = false;
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

                        //TODO:: Re-direct.
                        $scope.isLoading            = false;
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

                    animalService.updateAnimalDetail(animalDetail).then(
                        function () {
                            utilitiesService.utilities.alert(successContent);

                            $scope.isLoading = false;
                        },
                        function () {
                            utilitiesService.utilities.alert(failContent);

                            $scope.isLoading = false;
                        });
            }
        };

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