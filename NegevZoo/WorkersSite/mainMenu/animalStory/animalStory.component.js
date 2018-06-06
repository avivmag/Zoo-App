app.controller('zooAnimalStoryCtrl', ['$q', '$state', '$scope', '$stateParams', 'utilitiesService', 'animalService', 'fileUpload',

    function animalStoryController($q, $state, $scope, $stateParams, utilitiesService, animalService, fileUpload) {
        // If no enclosure was given in the state params, return to the enclosure's list.
        if (!angular.isDefined($stateParams.enclosure)) {
            $state.go('mainMenu.enclosures.list');
        }

        // Get all languages.
        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];
                
                // Initialize the animal story component.
                initializeComponent();

                // Run animal stories update.
                $scope.updateAnimalStories();
            });

        // Initializes the animal story component.
        function initializeComponent() {
            $scope.isLoading        = true;

            // If an animal story was given, set 'edit' mode as true.
            if (angular.isDefined($stateParams.animalStory)) {
                $scope.isEdit       = true;
            }

            // Get all state parameters to the scope.
            $scope.enclosure        = $stateParams.enclosure;
            $scope.baseURL          = app.baseURL;
            $scope.animalStory      = $stateParams.animalStory || { enclosureId: $scope.enclosure.id };

            // Initializes update animal stories function.
            $scope.updateAnimalStories    = function () {
                // If this is edit mode, get the story details from the server.
                if ($scope.isEdit) {
                    $scope.isLoading                = true;

                    // Get the story details.
                    animalService.getAnimalStoryDetailsById($scope.animalStory.id).then(
                        function (data) {
                            $scope.animalStoryDetails    = data.data;
    
                            // Fill details from all languages.
                            for (let i = 1; i <= app.languages.length; i++) {
                                if (!$scope.animalStoryDetails.some(ed => ed.language === i)) {
                                    $scope.animalStoryDetails.push({ animalStoryId: $scope.animalStory.id, language: i });
                                }
                            }
    
                            // Sort the data by languages.
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

            // Initializes the add animal story function.
            $scope.addAnimalStory   = function(animalStory) {
                // If the animal check failed, return.
                if (!checkAnimalStory(animalStory)) {
                    return;
                }

                $scope.isLoading            = true;

                // Initialize return statements.
                var successContent      = !$scope.isEdit ? 'הסיפור האישי נוסף בהצלחה!' : 'הסיפור האישי עודכן בהצלחה!';
                var failContent         = !$scope.isEdit ? 'התרחשה שגיאה בעת שמירת הסיפור האישי' : 'התרחשה שגיאה בעת עדכון הסיפור האישי';

                // Upload a profile picture (If needed).
                var pictureUploadQuery  = uploadProfilePicture($scope.profilePic, animalStory);

                var uploadPromises      = [pictureUploadQuery];

                $q.all(uploadPromises).then(
                    () => {
                        // Update the animal story.
                        animalService.updateAnimalStory(animalStory).then(
                            function (response) {
                                utilitiesService.utilities.alert(successContent);
                                
                                $scope.isLoading            = false;

                                // Refresh the data.
                                $stateParams.animalStory    = response.data;
                                $stateParams.enclosure      = $scope.enclosure;

                                // Refresh the component.
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

            $scope.confirmDeleteAnimalStory = function(animalStoryId) {
                utilitiesService.utilities.confirm({ title: 'מחיקת סיפור אישי', text: 'האם אתה בטוח שברצונך למחוק את הסיפור האישי?'}).then(
                    function () {
                        deleteAnimalStory(animalStoryId);
                    });
            }

            // Initialize the add animal story detail function.
            $scope.addAnimalStoryDetail       = function(animalStoryDetail) {
                $scope.isLoading            = true;
                
                // Initialize the return statements.
                var successContent      = !$scope.isEdit ? 'החיה נוספה בהצלחה!' : 'החיה עודכנה בהצלחה!';
                var failContent         = !$scope.isEdit ? 'התרחשה שגיאה בעת שמירת החיה' : 'התרחשה שגיאה בעת עדכון החיה';

                // Update the story detail.
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

        // Deletes an animal story.
        function deleteAnimalStory(animalStoryId) {
            $scope.isLoading                    = true;

            // Delete the animal.
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

        // Checks the animal story validity.
        function checkAnimalStory(checkAnimalStory) {
            // If no animal story was given, return.
            if (!checkAnimalStory) {
                return false;
            }

            // If the animal story did not have a name, or whitespace, return.
            if (!angular.isDefined(checkAnimalStory.name) || checkAnimalStory.name == '') {
                utilitiesService.utilities.alert('אנא בחר שם לסיפור החיה');

                return false;
            }

            return true;
        }

        // Uploads a profile picture.
        function uploadProfilePicture (picture, animalStory) {
            // If no picture was given, return.
            if (!angular.isDefined(picture)) {
                return;
            }

            $scope.isLoading        = true;

            // Initialize the upload URL.
            var uploadUrl           = 'animals/upload/story/';

            // Upload the profile picture.
            var fileUploadQuery     = fileUpload.uploadFileToUrl(picture, uploadUrl).then(
                (success)   => {
                    // Retrieve the picture's URL.
                    animalStory.pictureUrl  = success.data[0];

                    // Set the profile picture that the component holds to null.
                    $scope.profilePic       = null;
                },
                ()          => {
                    utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');
                });

            return fileUploadQuery;
        };
}]);