app.controller('zooSpecialEventsCtrl', ['$q', '$scope', 'fileUpload', 'utilitiesService', 'zooInfoService',
    function zooSpecialEventsController($q, $scope, fileUpload, utilitiesService, zooInfoService) {
        // Initialize the component.
        initializeComponent();
        
        // Get the languages.
        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];
                
                // Update the special events.
                $scope.updateSpecialEvents($scope.language);
            });
            
        // Initializes the component.
        function initializeComponent() {
            $scope.isLoading                = true;
            $scope.baseURL                  = app.baseURL;

            // Initialize the update special event function.
            $scope.updateSpecialEvents          = function (language) {
                $scope.language                 = language;

                // Get all the special events.
                zooInfoService.specialEvents.getAllSpecialEvents(language.id).then(
                    function (data) {
                        $scope.specialEvents    = data.data;
                        $scope.isLoading        = false;

                        // Add an empty event,
                        addEmptySpecialEvent($scope.specialEvents);
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים')

                        $scope.isLoading = false;
                    });
            };

            // Initialize the add event function.
            $scope.addEvent                     = function (event) {
                // Check the event's validity.
                if (!checkEvent(event)) {
                    return;
                }

                $scope.isLoading        = true;

                // Initialize the return statements.
                var successContent      = event.isNew ? 'האירוע נוסף בהצלחה!' : 'האירוע עודכן בהצלחה!';
                var failContent         = event.isNew ? 'התרחשה שגיאה בעת שמירת האירוע' : 'התרחשה שגיאה בעת עדכון האירוע';

                // Set the event's push message option.
                event.isPushMessage     = event.isPushMessage || false;

                // Upload the event's picture (if needed).
                var pictureUploadQuery  = uploadPicture(event.specialEventPic, event);

                var promises            = [pictureUploadQuery];

                $q.all(promises).then(
                    () => {
                        // Update the special event.
                        zooInfoService.specialEvents.updateSpecialEvent(event, event.isPushMessage).then(
                            function () {
                                utilitiesService.utilities.alert(successContent)
        
                                $scope.isLoading = false;
        
                                $scope.updateSpecialEvents($scope.language);
                            },
                            function () {
                                utilitiesService.utilities.alert(failContent)
        
                                $scope.isLoading = false;
                            });
                        });
            };

            // Initialize the confirm delete special event function.
            $scope.confirmDeleteSpecialEvent    = function (event, events) {
                utilitiesService.utilities.confirm({ title: 'מחיקת אירוע', text: 'האם אתה בטוח שברצונך למחוק אירוע זה?' }).then(
                    function () {
                        deleteEvent(event, events);
                    });

                return;
            }
        }

        // Checks the event's validity.
        function checkEvent(event) {
            // If no event was given, return.
            if (!event) {
                return false;
            }

            // If the event does not have a title, return.
            if (!angular.isDefined(event.title) || event.title === '') {
                utilitiesService.utilities.alert('אנא בחר כותרת לאירוע');

                return false;
            }

            // If the event does not have a description, return.
            if (!angular.isDefined(event.description) || event.description === '') {
                utilitiesService.utilities.alert('אנא בחר תיאור לאירוע');

                return false;
            }

            // If the event does not have a start or end date, return.
            if (!angular.isDefined(event.startDate) || !angular.isDefined(event.endDate)) {
                utilitiesService.utilities.alert('אנא בחר תאריכי התחלה וסיום לאירוע');

                return false;
            }

            // If the event's start date is after the end date, return.
            if (event.startDate > event.endDate) {
                utilitiesService.utilities.alert('תאריך ההתחלה מוכרח להיות מוקדם מתאריך הסיום של האירוע');

                return false;
            }

            return true;
        }

        // Deletes an event.
        function deleteEvent(event, events) {
            zooInfoService.specialEvents.deleteSpecialEvent(event.id).then(
                function () {
                    utilitiesService.utilities.alert('התוכן נמחק בהצלחה');

                    // Remove the event from the event's array.
                    events.splice(events.indexOf(event), 1);
                },
                function () {
                    utilitiesService.utilities.alert('התרחשה שגיאה בעת מחיקת התוכן');
                });
        }

        // Adds an empty special event.
        function addEmptySpecialEvent(specialEvents) {
            var startDate   = new Date();
            var endDate     = new Date();

            specialEvents.push({ isNew: true, language: $scope.language.id, startDate, endDate, id: 0 });
        }

        // Uploads the event's picture.
        function uploadPicture (picture, event) {
            // If no picture was given, return.
            if (!angular.isDefined(picture)) {
                return;
            }

            $scope.isLoading        = true;

            // Set the upload url.
            var uploadUrl           = 'specialEvents/upload';

            // Upload the file.
            var fileUploadQuery     = fileUpload.uploadFileToUrl(picture, uploadUrl).then(
                (success)   => {
                    // Get the image's url.
                    event.imageUrl          = success.data[0];
                },
                ()          => {
                    utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');
                });

            return fileUploadQuery;
        };
}])
.directive('zooEvents', function () {
    return {
        templateUrl: 'mainMenu/specialEvents/specialEvents.html'
    };
});