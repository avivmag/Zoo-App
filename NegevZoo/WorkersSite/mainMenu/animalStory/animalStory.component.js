app.controller('zooAnimalStoryCtrl', ['$q', '$state', '$scope', '$stateParams', 'utilitiesService', 'animalService', 'fileUpload',

    function animalStoryController($q, $state, $scope, $stateParams, utilitiesService, animalService, fileUpload) {
        if (!angular.isDefined($stateParams.enclosure)) {
            $state.go('mainMenu.enclosures.list');
        }

        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];
                
                initializeComponent();
                $scope.updateAnimalStories();
            });

        function initializeComponent() {
            $scope.isLoading        = true;

            if (angular.isDefined($stateParams.animalStory)) {
                $scope.isEdit       = true;
            }

            $scope.enclosure        = $stateParams.enclosure;
            $scope.baseURL          = app.baseURL;
            $scope.animalStory      = $stateParams.animalStory || { enclosureId: $scope.enclosure.id };

            $scope.updateAnimalStories    = function () {
                if ($scope.isEdit) {
                    $scope.isLoading                = true;

                    animalService.getAnimalStoryDetailsById($scope.animalStory.id).then(
                        function (data) {
                            $scope.animalStoryDetails    = data.data;
    
                            for (let i = 1; i <= 4; i++) {
                                if (!$scope.animalStoryDetails.some(ed => ed.language === i)) {
                                    $scope.animalStoryDetails.push({ animalStoryId: $scope.animalStory.id, language: i });
                                }
                            }
    
                            $scope.animalStoryDetails.sort(function(a, b) { return a.language-b.language; });
    
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

            $scope.addAnimalStory   = function(animalStory) {
                $scope.isLoading            = true;
                    var successContent      = !$scope.isEdit ? 'הסיפור האישי נוסף בהצלחה!' : 'הסיפור האישי עודכן בהצלחה!';
                    var failContent         = !$scope.isEdit ? 'התרחשה שגיאה בעת שמירת הסיפור האישי' : 'התרחשה שגיאה בעת עדכון הסיפור האישי';

                    var pictureUploadQuery  = uploadProfilePicture($scope.profilePic, animalStory);

                    var uploadPromises      = [pictureUploadQuery];

                    $q.all(uploadPromises).then(
                        () => {
                            animalService.updateAnimalStory(animalStory).then(
                                function (response) {
                                    utilitiesService.utilities.alert(successContent);
                                    
                                    $scope.isLoading            = false;

                                    $stateParams.animalStory    = response.data;
                                    $stateParams.enclosure      = $scope.enclosure;

                                    initializeComponent();
                                    $scope.updateAnimalStories();
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

            $scope.deleteAnimalStory = function(animalStoryId) {
                $scope.isLoading                    = true;

                animalService.deleteAnimalStory(animalStoryId).then(
                    function() {
                        utilitiesService.utilities.alert("הסיפור האישי נמחק בהצלחה!");

                        $state.go('mainMenu.enclosures.enclosure', { enclosure: $scope.enclosure });
                    },

                    function () {
                        utilitiesService.utilities.alert("חלה שגיאה במחיקת הסיפור האישי.");

                        $scope.isLoading            = false;
                    });
            }

            $scope.addAnimalStoryDetail       = function(animalStoryDetail) {
                $scope.isLoading            = true;
                    var successContent      = !$scope.isEdit ? 'החיה נוספה בהצלחה!' : 'החיה עודכנה בהצלחה!';
                    var failContent         = !$scope.isEdit ? 'התרחשה שגיאה בעת שמירת החיה' : 'התרחשה שגיאה בעת עדכון החיה';

                    animalService.updateAnimalStoryDetail(animalStoryDetail).then(
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

        function uploadProfilePicture (picture, animalStory) {
            if (!angular.isDefined(picture)) {
                return;
            }

            $scope.isLoading        = true;

            var uploadUrl           = 'animals/upload/story/';

            var fileUploadQuery     = fileUpload.uploadFileToUrl(picture, uploadUrl).then(
                (success)   => {
                    animalStory.pictureUrl  = success.data[0];

                    $scope.profilePic       = null;
                },
                ()          => {
                    utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');
                });

            return fileUploadQuery;
        };
}]);