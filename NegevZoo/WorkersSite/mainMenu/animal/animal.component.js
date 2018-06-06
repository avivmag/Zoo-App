app.controller('zooAnimalCtrl', ['$q', '$state', '$scope', '$stateParams', 'utilitiesService', 'animalService', 'fileUpload',
    function animalController($q, $state, $scope, $stateParams, utilitiesService, animalService, fileUpload) {
        // If no enclosure was given in the state params, return to the enclosure's list.
        if (!angular.isDefined($stateParams.enclosure)) {
            $state.go('mainMenu.enclosures.list');
        }

        // Get all languages.
        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];
                
                // Initialize the animal's component.
                initializeComponent();

                // Run animal's update.
                $scope.updateAnimals();
            });

   
        // Initializes the animal component.
        function initializeComponent() {
            $scope.isLoading        = true;

            // If an animal was given, set 'edit' mode to true.
            if (angular.isDefined($stateParams.animal)) {
                $scope.isEdit       = true;
            }

            // Get all state parameters to the scope.
            $scope.enclosure        = $stateParams.enclosure;
            $scope.baseURL          = app.baseURL;
            $scope.animal           = $stateParams.animal || { enclosureId: $scope.enclosure.id };

            // Get the preservation types.
            $scope.preservation     = animalService.getPreservation();

            // Initialize update animals function.
            $scope.updateAnimals    = function () {
                // If this is edit mode, get the animal's details from the server.
                if ($scope.isEdit) {
                    $scope.isLoading                = true;

                    animalService.getAnimalDetailsById($scope.animal.id).then(
                        function (data) {
                            $scope.animalDetails    = data.data;
    
                            // Fill details from all languages.
                            for (let i = 1; i <= $scope.languages.length; i++) {
                                if (!$scope.animalDetails.some(ed => ed.language === i)) {
                                    $scope.animalDetails.push({ animalId: $scope.animal.id, language: i });
                                }
                            }
    
                            // Sort the data by languages.
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

            // Initialize add animal function.
            $scope.addAnimal        = function(animal) {
                // If the animal check failed, return.
                if (!checkAnimal(animal)) {
                    return;
                }

                $scope.isLoading        = true;

                // Initialize return statements.
                var successContent      = !$scope.isEdit ? 'החיה נוספה בהצלחה!' : 'החיה עודכנה בהצלחה!';
                var failContent         = !$scope.isEdit ? 'התרחשה שגיאה בעת שמירת החיה' : 'התרחשה שגיאה בעת עדכון החיה';

                // Update the animal's profile picture (if needed).
                var pictureUploadQuery  = uploadProfilePicture($scope.profilePic, animal);

                var uploadPromises      = [pictureUploadQuery];

                $q.all(uploadPromises).then(
                    () => {
                        // Update the animal.
                        animalService.updateAnimal(animal).then(
                            function (response) {
                                utilitiesService.utilities.alert(successContent);
                                
                                $scope.isLoading        = false;

                                // Refresh the data.
                                $stateParams.animal     = response.data;
                                $stateParams.enclosure  = $scope.enclosure;

                                // Refresh the component.
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

            // Confirm's a deletion of an animal.
            $scope.confirmDeleteAnimal = function(animalId) {
                utilitiesService.utilities.confirm({ title: 'מחיקת חיה', text: 'האם אתה בטוח שברצונך למחוק חיה זו?' }).then(
                    function () {
                        deleteAnimal(animalId);
                    });

                return;
            }

            // Initialize add animal detail function.
            $scope.addAnimalDetail       = function(animalDetail) {
                $scope.isLoading            = true;

                // Initialize the return statements.
                var successContent      = $scope.page === 'create' ? 'החיה נוספה בהצלחה!' : 'החיה עודכנה בהצלחה!';
                var failContent         = $scope.page === 'create' ? 'התרחשה שגיאה בעת שמירת החיה' : 'התרחשה שגיאה בעת עדכון החיה';

                // Upload audio file (if needed).
                var audioUploadQuery    = uploadAudioFile(animalDetail);

                $q.all([audioUploadQuery]).then(
                    () => {
                        // Update the animal detail.
                        animalService.updateAnimalDetail(animalDetail).then(
                            function () {
                                utilitiesService.utilities.alert(successContent);

                                // Delete the audio object that the controller holds.
                                delete $scope.audio;

                                $scope.isLoading = false;
                            },
                            function () {
                                utilitiesService.utilities.alert(failContent);

                                $scope.isLoading = false;
                            });
                    },
                    () => utilitiesService.utilities.alert(failContent));
            }

            // Plays or stops the audio file.
            $scope.playSound                = function(audioFile) {
                // If the controller does not hold an audio instance, initialize one.
                if (!$scope.audio) {
                    $scope.audio = new Audio($scope.baseURL + audioFile);
                }
                
                // If the audio instance is paused, play it.
                if ($scope.audio.paused) {
                    $scope.audio.play();
                }
                // Otherwise, pause the audio instance and set it's time to 0.
                else {
                    $scope.audio.pause();
                    $scope.audio.currentTime    = 0;
                }
            };
        };

        // Deletes an animal.
        function deleteAnimal(animalId) {
            $scope.isLoading                    = true;

            // Delete the animal.
            animalService.deleteAnimal(animalId).then(
                function() {
                    utilitiesService.utilities.alert("החיה נמחקה בהצלחה!");

                    // Go back the the enclosure page.
                    $state.go('mainMenu.enclosures.enclosure', { enclosure: $scope.enclosure });
                },

                function () {
                    utilitiesService.utilities.alert("חלה שגיאה במחיקת החיה.");

                    $scope.isLoading            = false;
                });
        }

        
        // Checks if the animal input is valid.
        function checkAnimal(animal) {
            // If no animal was given, return.
            if (!animal) {
                return false;
            }

            // If the animal does not have a name, or it is whitespace, return.
            if (!angular.isDefined(animal.name) || animal.name == '') {
                utilitiesService.utilities.alert('אנא בחר שם לחיה');

                return false;
            }

            // If the animal does not have preservation, return.
            if (!angular.isDefined(animal.preservation)) {
                utilitiesService.utilities.alert('אנא בחר רמת שימור לחיה');

                return false;
            }

            return true;
        }

        // Uploads an audio file.
        function uploadAudioFile(animalDetail) {
            // If no audio file was given, return.
            if (!angular.isDefined(animalDetail.animalAudioFile) || animalDetail.animalAudioFile === null) {
                return;
            }

            $scope.isLoading        = true;

            // Set the upload URL.
            var uploadUrl           = 'animals/upload/audio/false';

            // Upload the file.
            var fileUploadQuery     = fileUpload.uploadFileToUrl(animalDetail.animalAudioFile, uploadUrl).then(
                (success)   => {
                    // Retrieve the audio file's URL.
                    animalDetail.audioUrl    = success.data[0];
                },
                ()          => {
                    utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');
                });

            return fileUploadQuery;
        }

        // Uploads a profile picture.
        function uploadProfilePicture (picture, animal) {
            // If no profile picture was given, return.
            if (!angular.isDefined(picture)) {
                return;
            }

            $scope.isLoading        = true;

            // Set the upload URL.
            var uploadUrl           = 'animals/upload/pictures';

            // Upload the file.
            var fileUploadQuery     = fileUpload.uploadFileToUrl(picture, uploadUrl).then(
                (success)   => {
                    // Retrieve the picture's URL.
                    animal.pictureUrl    = success.data[0];

                    // Set the file object to null.
                    $scope.profilePic       = null;
                },
                ()          => {
                    utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');
                });

            return fileUploadQuery;
        };
}]);