app.controller('zooEnclosureCtrl', ['$q', '$state', '$scope', '$stateParams', 'utilitiesService', 'enclosureService', 'animalService', 'mapViewService', 'fileUpload',

    function enclosureController($q, $state, $scope, $stateParams, utilitiesService, enclosureService, animalService, mapViewService, fileUpload) {
        // Get all languages.
        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];
                
                // Initialize the enclosure component.
                initializeComponent();
            });
            

        function initializeComponent() {
            $scope.selectedEnclosure    = $stateParams.enclosure;
            $scope.baseURL              = app.baseURL;
            $scope.hours                = utilitiesService.timeSpan.getHours();
            $scope.minutes              = utilitiesService.timeSpan.getMinutes();
            $scope.days                 = utilitiesService.getDays();

            $scope.selectedEnclosure    = $stateParams.enclosure || { };
            $scope.isEdit               = angular.isDefined($stateParams.enclosure) ? true : false;

            // If this is edit mode, get all the data of the enclosure.
            if ($scope.isEdit) {
                $scope.isLoading            = true;

                // Get the enclosure's details.
                var detailsQuery            = enclosureService.enclosureDetails.getEnclosureDetailsById($scope.selectedEnclosure.id).then(
                    function (data) {
                        $scope.enclosureDetails = data.data;

                        // Fill details from all enclosures.
                        for (let i = 1; i <= 4; i++) {
                            if (!$scope.enclosureDetails.some(ed => ed.language === i)) {
                                $scope.enclosureDetails.push({ encId: $scope.selectedEnclosure.id, language: i });
                            }
                        }

                        // Sort the details by language.
                        $scope.enclosureDetails.sort(function(a, b) { return a.language-b.language; });
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                    });

                // Get all the enclosure's videos.
                var videosQuery             = enclosureService.enclosures.getEnclosureVideosById($scope.selectedEnclosure.id).then(
                    function (data) {
                        $scope.selectedEnclosure.videos = data.data;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                    });

                // Get all the enclosure's pictures.
                var picturesQuery           = enclosureService.enclosures.getEnclosurePicturesById($scope.selectedEnclosure.id).then(
                    function (data) {
                        $scope.selectedEnclosure.pictures = data.data;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                    });

                // Get all the enclosure's animals.
                var animalsQuery            = animalService.getAnimalsByEnclosure($scope.selectedEnclosure.id).then(
                    function (animals) {
                        $scope.selectedEnclosure.animals = animals.data;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                    });

                // Get all the enclosure's animal stories..
                var animalStoriesQuery      = animalService.getAnimalStoriesByEnclosure($scope.selectedEnclosure.id).then(
                    function (animalStories) {
                        $scope.selectedEnclosure.animalStories = animalStories.data;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                    });
                
                // Get all the enclosure's recurring events.
                var recurringEventsQuery    = enclosureService.enclosureDetails.getRecurringEvents($scope.selectedEnclosure.id, $scope.language.id).then(
                    function (data) {
                        $scope.selectedEnclosure.recurringEvents = data.data;

                        // Parse the recurring event's time.
                        for (let re of $scope.selectedEnclosure.recurringEvents) {
                            re.startTime    = utilitiesService.timeSpan.parseTimeSpan(re.startTime);
                            re.endTime      = utilitiesService.timeSpan.parseTimeSpan(re.endTime);
                        }

                        // Add an empty recurring event for user's add option.
                        addEmptyRecurringEvent($scope.selectedEnclosure.recurringEvents);
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                    });

                var promises = [detailsQuery, videosQuery, picturesQuery, animalsQuery, recurringEventsQuery, animalStoriesQuery];

                // When all the fetching was done, set loading to false.
                $q.all(promises).then(
                    () => $scope.isLoading = false,
                    () => $scope.isLoading = false);
            }
            
            // Initialize the open map function.
            $scope.openMap                      = function(ev, selectedEnclosure) {
                // Upload a new enclosure icon (if needed).
                var promises = [uploadIcon($scope.iconPic, selectedEnclosure)];

                // When upload was done, open map.
                $q.all(promises).then(
                    () => {
                        $scope.isLoading = false;
                        
                        // Open the map service.
                        mapViewService.showMap(ev, selectedEnclosure, 'markerIconUrl', 'markerX', 'markerY', ($scope.oldMarker || selectedEnclosure.markerIconUrl)).then(function(clickPosition) {
                            // Get the clicked coordinates.
                            if (angular.isDefined(clickPosition)) {
                                selectedEnclosure.markerX       = Math.floor(clickPosition.width * clickPosition.ratio);
                                selectedEnclosure.markerY       = Math.floor(clickPosition.height * clickPosition.ratio);
                            }
                        });
                    });
            }

            // Initialize the add enclosure function.
            $scope.addEnclosure                 = function(enclosure) {
                $scope.isLoading        = true;

                // Initialize the return statements.
                var successContent      = !$scope.isEdit ? 'המתחם נוסף בהצלחה!' : 'המתחם עודכן בהצלחה!';
                var failContent         = !$scope.isEdit ? 'התרחשה שגיאה בעת שמירת המתחם' : 'התרחשה שגיאה בעת עדכון המתחם';

                // Upload the enclosure profile picture (if needed).
                var pictureUploadQuery  = uploadProfilePicture($scope.profilePic, enclosure);

                // Upload the enclosure icon picture (if needed).
                var iconUploadQuery     = uploadIcon($scope.iconPic, enclosure);

                var uploadPromises      = [pictureUploadQuery, iconUploadQuery];

                // When the upload's were done, update the enclosure.
                $q.all(uploadPromises).then(
                    () => {
                        // Check the enclosure's validity.
                        if (!checkEnclosure(enclosure)) {
                            $scope.isLoading = false;

                            return;
                        }
                        
                        // Update the enclosure.
                        enclosureService.enclosures.updateEnclosure(enclosure).then(
                            function (response) {
                                utilitiesService.utilities.alert(successContent);
                                
                                // Referesh the component's data.
                                $stateParams.enclosure = response.data;
                                initializeComponent();

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

            // Initialize the confirm delete enclosure function.
            $scope.confirmDeleteEnclosure       = function(encId) {
                utilitiesService.utilities.confirm({ title: 'מחיקת מתחם', text: 'האם אתה בטוח שברצונך למחוק מתחם זה?' }).then(
                    function () {
                        deleteEnclosure(encId);
                    });

                return;
            }

            // Initialize the add enclosure detail function.
            $scope.addEnclosureDetail           = function(enclosureDetail) {
                $scope.isLoading            = true;

                // Upload an audio file (if needed).
                var audioUploadQuery    = uploadAudioFile(enclosureDetail);

                // When upload is done, add the enclosure detail.
                $q.all([audioUploadQuery]).then(
                    () => {
                        enclosureService.enclosureDetails.updateEnclosureDetail(enclosureDetail).then(
                            function () {
                                utilitiesService.utilities.alert('המתחם עודכן בהצלחה!');

                                // Delete the file that the component holds.
                                delete $scope.audio;

                                $scope.isLoading = false;
                            },
                            function () {
                                utilitiesService.utilities.alert('התרחשה שגיאה בעת עדכון המתחם');
    
                                $scope.isLoading = false;
                        });
                    },
                    () => utilitiesService.utilities.alert(failContent));
            }

            // Initialize the add enclousre video function.
            $scope.addEnclosureVideo            = function(selectedEnclosure, videoUrl) {
                // Check for video url validity.
                if (!checkVideoUrl(videoUrl)) {
                    return;
                }

                $scope.isLoading        = true;

                // Fix the watch string to save in database.
                var watchString         = videoUrl.split('watch?v=')[1].split('&')[0];
                
                // Build the enclosure video object.
                var enclosureVideo      = { enclosureId: selectedEnclosure.id, videoUrl: watchString };

                // Add the enclousre video.
                enclosureService.enclosures.updateVideoById(enclosureVideo).then(
                    function (updatedVideo) {
                        utilitiesService.utilities.alert('הסרטון הועלה בהצלחה!');

                        selectedEnclosure.videos.push(updatedVideo.data);

                        $scope.isLoading    = false;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה בעת העלאת הסרטון.');

                        $scope.isLoading    = false;
                    }
                )
            };

            // Initialize the upload enclosure pictures function.
            $scope.uploadEnclosurePictures      = function(pictures, enclosure) {
                // If no picture was given, return.
                if (!angular.isDefined(pictures)) {
                    return;
                }
    
                $scope.isLoading            = true;
    
                var uploadUrl               = `enclosures/${enclosure.id}/upload/bulk`;
    
                // Upload the pictures.
                var fileUploadQuery         = fileUpload.uploadFileToUrl(pictures, uploadUrl).then(
                    (success)   => {
                        utilitiesService.utilities.alert("התמונות הועלו בהצלחה.");
                        enclosure.pictures  = enclosure.pictures.concat(success.data);

                        $scope.isLoading    = false;
                    },
                    ()          => {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');

                        $scope.isLoading    = false;
                    });
    
            }

            // Initialize the confirm delete picture function.
            $scope.confirmDeletePicture         = function(selectedEnclosureId, picture, pictures) {
                utilitiesService.utilities.confirm({ title: 'מחיקת תמונה', text: 'האם אתה בטוח שברצונך למחוק תמונה זו?' }).then(
                    function () {
                        deletePicture(selectedEnclosureId, picture, pictures);
                    });

                return;
            }

            // Initialize the confirm delete video function.
            $scope.confirmDeleteVideo           = function(selectedEnclosureId, video, videos) {
                utilitiesService.utilities.confirm({ title: 'מחיקת סרטון', text: 'האם אתה בטוח שברצונך למחוק סרטון זה?' }).then(
                    function () {
                        deleteVideo(selectedEnclosureId, video, videos);
                    });

                return;
            }

            // Initialize the update recurring event function.
            $scope.updateRecurringEvents        = function(selectedEnclosure, language) {
                $scope.language             = language;

                // Get all the recurring events.
                enclosureService.enclosureDetails.getRecurringEvents(selectedEnclosure.id, language.id).then(
                    function (data) {
                        $scope.selectedEnclosure.recurringEvents = data.data;

                        // Parse the recurring event's time.
                        for (let re of $scope.selectedEnclosure.recurringEvents) {
                            re.startTime    = utilitiesService.timeSpan.parseTimeSpan(re.startTime);
                            re.endTime      = utilitiesService.timeSpan.parseTimeSpan(re.endTime);
                        }

                        // Add an empty recurring event for user's add option.
                        addEmptyRecurringEvent($scope.selectedEnclosure.recurringEvents);
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                    });
            };

            // Initialize the add recurring event function.
            $scope.addRecurringEvent            = function(recurringEvent) {
                // Check the recurring event's validity.
                if (!checkRecurringEvent(recurringEvent)) {
                    return false;
                }

                // Initialize the return statements.
                var successContent      = recurringEvent.isNew ? 'האירוע החוזר נוסף בהצלחה!' : 'האירוע החוזר עודכן בהצלחה!';
                var failContent         = recurringEvent.isNew ? 'התרחשה שגיאה בעת שמירת האירוע החוזר' : 'התרחשה שגיאה בעת עדכון האירוע החוזר';

                // Stringify the event's time.
                recurringEvent.startTime   = utilitiesService.timeSpan.stringifyTimeSpan(recurringEvent.startTime);
                recurringEvent.endTime     = utilitiesService.timeSpan.stringifyTimeSpan(recurringEvent.endTime);

                // Update the recurring event.
                enclosureService.enclosureDetails.updateRecurringEvent(recurringEvent).then(
                    function () {
                        utilitiesService.utilities.alert(successContent);

                        // Refresh the recurring events.
                        $scope.updateRecurringEvents($scope.selectedEnclosure, $scope.language);

                        // Parse back the recurring event's time.
                        recurringEvent.startTime   = utilitiesService.timeSpan.parseTimeSpan(recurringEvent.startTime);
                        recurringEvent.endTime     = utilitiesService.timeSpan.parseTimeSpan(recurringEvent.endTime);
                    },
                    function () {
                        utilitiesService.utilities.alert(failContent);

                        // Parse back the recurring event's time.
                        recurringEvent.startTime   = utilitiesService.timeSpan.parseTimeSpan(recurringEvent.startTime);
                        recurringEvent.endTime     = utilitiesService.timeSpan.parseTimeSpan(recurringEvent.endTime);
                    });
            };

            // Initialize the confirm delete recurring event function.
            $scope.confirmDeleteRecurringEvent  = function(selectedEnclosureId, recurringEvents, recurringEvent) {
                utilitiesService.utilities.confirm({ title: 'מחיקת אירוע חוזר', text: 'האם אתה בטוח שברצונך למחוק אירוע חוזר זה?' }).then(
                    function () {
                        deleteRecurringEvent(selectedEnclosureId, recurringEvents, recurringEvent);
                    });

                return;
            }

            // Initialize the play sound function.
            $scope.playSound                    = function(audioFile) {
                // If no audio object was initialized, initialize it.
                if (!$scope.audio) {
                    $scope.audio = new Audio($scope.baseURL + audioFile);
                }
                
                // If the audio file was paused, play it.
                if ($scope.audio.paused) {
                    $scope.audio.play();
                }
                // Otherwise, if the audio file was playing, pause it and set it's current time to 0.
                else {
                    $scope.audio.pause();
                    $scope.audio.currentTime    = 0;
                }
            };
        };

        // deletes the enclosure.
        function deleteEnclosure(encId) {
            $scope.isLoading        = true;

            // Delete the enclosure.
            enclosureService.enclosures.deleteEnclosure(encId).then(
                function() {
                    utilitiesService.utilities.alert("המתחם נמחק בהצלחה!");

                    $state.go('mainMenu.enclosures.list')
                },

                function () {
                    utilitiesService.utilities.alert("חלה שגיאה במחיקת המתחם.");

                    $scope.isLoading            = false;
                });
        }

        // deletes a recurring event.
        function deleteRecurringEvent(selectedEnclosureId, recurringEvents, recurringEvent) {
            // Delete the recurring event.
            enclosureService.enclosureDetails.deleteRecurringEvent(selectedEnclosureId, recurringEvent.id).then(
                () => {
                    utilitiesService.utilities.alert("האירוע החוזר נמחק בהצלחה.");

                    // Remove the recurring event from the recurring events array.
                    recurringEvents.splice(recurringEvents.indexOf(recurringEvent), 1);
                },
                () => {
                    utilitiesService.utilities.alert("אירעה שגיאה בעת מחיקת האירוע החוזר.");
                });
        }

        // Deletes a video.
        function deleteVideo(selectedEnclosureId, video, videos) {
            $scope.isLoading = true;

            // Delete the video.
            enclosureService.enclosures.deleteEnclosureVideo(selectedEnclosureId, video.id).then(
                () => {
                    utilitiesService.utilities.alert("הסרטון נמחק בהצלחה.");

                    // Remove the deleted video from the videos array.
                    videos.splice(videos.indexOf(video), 1);

                    $scope.isLoading = false;
                },
                () => {
                    utilitiesService.utilities.alert("אירעה שגיאה בעת מחיקת הסרטון.");

                    $scope.isLoading = false;
                }
            )
        }

        // Deletes a picture.
        function deletePicture(selectedEnclosureId, picture, pictures) {
            $scope.isLoading = true;

            // Delete the picture.
            enclosureService.enclosures.deleteEnclosurePicture(selectedEnclosureId, picture.id).then(
                () => {
                    utilitiesService.utilities.alert("התמונה נמחקה בהצלחה.");

                    // Remove the picture from the picture's array.
                    pictures.splice(pictures.indexOf(picture), 1);
                    
                    $scope.isLoading = false;
                },
                () => {
                    utilitiesService.utilities.alert("אירעה שגיאה בעת מחיקת התמונה.");

                    $scope.isLoading = false;
                });
        }

        // Checks the enclosure's validity.
        function checkEnclosure(enclosure) {
            // If no name was defined, return.
            if (!angular.isDefined(enclosure.name) || enclosure.name == '') {
                utilitiesService.utilities.alert('אנא בחר שם למתחם');

                return false;
            }

            // If the marker's were invalid, return.
            if ((enclosure.markerX !== undefined && enclosure.markerX < 0) ||
                (enclosure.markerY !== undefined && enclosure.markerY < 0)) {
                    utilitiesService.utilities.alert('הנקודות שנבחרו למיקום המתחם אינן חוקיות');

                    return false;
            }

            // If the user had selected markers, but without an icon, return.
            if ((enclosure.markerX !== undefined && enclosure.markerX !== null) && 
                (enclosure.markerY !== undefined && enclosure.markerY !== null) &&
                (enclosure.markerIconUrl === undefined || enclosure.markerIconUrl === null)) {
                    utilitiesService.utilities.alert('אין לבחור מיקום מתחם ללא העלאת אייקון.');

                    return false;
            }

            // If the used had selected an icon but did not place the enclosure on the map, return.
            if ((enclosure.markerX === undefined || enclosure.markerX === null || enclosure.markerY === undefined || enclosure.markerY === null) &&
                (enclosure.markerIconUrl !== undefined && enclosure.markerIconUrl !== null)) {
                    utilitiesService.utilities.alert('אין להעלות אייקון ללא בחירת מיקום.');

                    return false;
            }

            return true;
        }

        // Checks the video url validity.
        function checkVideoUrl(videoUrl) {
            // If no video url was given, return.
            if (!videoUrl) {
                return false;
            }

            // If the url is not of youtube's, or the url is invalid by youtube's standards, return.
            if ((videoUrl.indexOf('https://www.youtube.com/') === -1) || (videoUrl.indexOf('watch?v=') === -1)) {
                utilitiesService.utilities.alert('אנא הכנס לינק תקין של יוטיוב.');

                return false;
            }

            return true;
        }

        // Checks the recurring event's validity.
        function checkRecurringEvent(recurringEvent) {
            // If no recurring event was given, return.
            if (!recurringEvent) {
                return false;
            }

            // If the recurring event does not have a title, return.
            if (!angular.isDefined(recurringEvent.title) || recurringEvent.title === '') {
                utilitiesService.utilities.alert('אנא בחר כותרת לאירוע החוזר.');

                return false;
            }

            // If the recurring event does not have a description, return.
            if (!angular.isDefined(recurringEvent.description) || recurringEvent.description === '') {
                utilitiesService.utilities.alert('אנא בחר תיאור לאירוע החוזר.');

                return false;
            }

            // If the recurring event does not have a day, or the day of week is invalid, return.
            if (recurringEvent.day === undefined || recurringEvent.day < 1 || recurringEvent.day > 7) {
                utilitiesService.utilities.alert('אנא בחר יום תקין לאירוע החוזר.');

                return false;
            }

            // If the recurring event start time is invalid, return.
            if (recurringEvent.startTime === undefined || 
                recurringEvent.startTime.hour === undefined ||
                recurringEvent.startTime.minute === undefined) {
                    utilitiesService.utilities.alert('אנא בחר שעת התחלה תקינה.');

                    return false;
            }

            // If the recurring event end time is invalid, return.
            if (recurringEvent.endTime === undefined || 
                recurringEvent.endTime.hour === undefined ||
                recurringEvent.endTime.minute === undefined) {
                    utilitiesService.utilities.alert('אנא בחר שעת סוף תקינה.');

                    return false;
            }

            return true;
        }

        // Adds an empty recurring event.
        function addEmptyRecurringEvent(recurringEvents) {
            recurringEvents.push({ isNew: true, language: $scope.language.id, id: 0, enclosureId: $scope.selectedEnclosure.id });
        }

        // Uploads an enclosure profile picture.
        function uploadProfilePicture(picture, enclosure) {
            // If no picture was given, return.
            if (!angular.isDefined(picture) || picture === null) {
                return;
            }

            $scope.isLoading        = true;

            // Set the upload url.
            var uploadUrl           = 'enclosures/upload/pictures';

            // Upload the picture.
            var fileUploadQuery     = fileUpload.uploadFileToUrl(picture, uploadUrl).then(
                (success)   => {
                    enclosure.pictureUrl    = success.data[0];

                    $scope.profilePic       = null;
                },
                ()          => {
                    utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');
                });

            return fileUploadQuery;
        };

        // Uploads an enclosure audio file.
        function uploadAudioFile(enclosureDetail) {
            // If no audio file was selected, return.
            if (!angular.isDefined(enclosureDetail.enclosureAudioFile) || enclosureDetail.enclosureAudioFile === null) {
                return;
            }

            $scope.isLoading        = true;

            // Set the upload url.
            var uploadUrl           = 'enclosures/upload/audio/false';

            // Upload the file.
            var fileUploadQuery     = fileUpload.uploadFileToUrl(enclosureDetail.enclosureAudioFile, uploadUrl).then(
                (success)   => {
                    enclosureDetail.audioUrl    = success.data[0];
                },
                ()          => {
                    utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');
                });

            return fileUploadQuery;
        }

        // Uploads an enclosure icon.
        function uploadIcon(icon, enclosure) {
            // If no icon was given, return.
            if (!angular.isDefined(icon) || icon === null) {
                return;
            }

            $scope.isLoading        = true;

            // Set the upload url.
            var uploadUrl           = 'enclosures/upload/markers';

            // Upload the file.
            var fileUploadQuery     = fileUpload.uploadFileToUrl(icon, uploadUrl).then(
                (success)   => {
                    // Get the old enclosure's marker if the user decides to open the map.
                    $scope.oldMarker            = $scope.oldMarker || enclosure.markerIconUrl;

                    enclosure.markerIconUrl     = success.data[0];

                    $scope.iconPic              = null;
                },
                ()          => {
                    utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');
                });

            return fileUploadQuery;
        };
}]);