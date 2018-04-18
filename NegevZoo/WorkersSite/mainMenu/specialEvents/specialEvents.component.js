app.controller('zooEventsCtrl', ['$q', '$scope', '$mdDialog', 'fileUpload', 'utilitiesService', 'zooInfoService',
    function zooSpecialEventsController($q, $scope, $mdDialog, fileUpload, utilitiesService, zooInfoService) {
        initializeComponent();

        function initializeComponent() {
            $scope.languages            = app.languages;
            $scope.language             = $scope.languages[0];
            $scope.baseURL              = app.baseURL;

            $scope.updateSpecialEvents          = function (language) {
                $scope.language                 = language;
                $scope.isLoading                = true;

                zooInfoService.specialEvents.getAllSpecialEvents(language.id).then(
                    function (data) {
                        $scope.specialEvents    = data.data;
                        $scope.isLoading        = false;

                        addEmptySpecialEvent($scope.specialEvents);
                    },
                    function () {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .textContent('אירעה שגיאה במהלך טעינת הנתונים')
                                .ok('סגור')
                        );

                        $scope.isLoading = false;
                    });
            };

            $scope.addEvent                     = function (event) {
                $scope.isLoading        = true;
                var successContent      = event.isNew ? 'האירוע נוסף בהצלחה!' : 'האירוע עודכן בהצלחה!';
                var failContent         = event.isNew ? 'התרחשה שגיאה בעת שמירת האירוע' : 'התרחשה שגיאה בעת עדכון האירוע';

                event.isPushMessage     = event.isPushMessage || false;

                var pictureUploadQuery  = uploadPicture(event.specialEventPic, event);

                var promises            = [pictureUploadQuery];

                $q.all(promises).then(
                    () => {
                        zooInfoService.specialEvents.updateSpecialEvent(event, event.isPushMessage).then(
                            function () {
                                $mdDialog.show(
                                    $mdDialog.alert()
                                        .clickOutsideToClose(true)
                                        .textContent(successContent)
                                        .ok('סגור')
                                );
        
                                $scope.isLoading = false;
        
                                $scope.updateSpecialEvents($scope.language);
                            },
                            function () {
                                $mdDialog.show(
                                    $mdDialog.alert()
                                        .clickOutsideToClose(true)
                                        .textContent(failContent)
                                        .ok('סגור')
                                );
        
                                $scope.isLoading = false;
                            });
                        });
            };

            $scope.confirmDeleteSpecialEvent    = function (ev, event, events) {
                var confirm = $mdDialog.confirm()
                    .title('האם אתה בטוח שברצונך למחוק את אירוע זה?')
                    .textContent('לאחר המחיקה, לא תוכל להחזירו אלא ליצור אותו מחדש')
                    .targetEvent(ev)
                    .ok('אישור')
                    .cancel('ביטול');

                $mdDialog.show(confirm).then(function () {
                    deleteEvent(event, events);
                });
            }

            $scope.updateSpecialEvents($scope.language);
        }

        function deleteEvent(event, events) {
            zooInfoService.specialEvents.deleteSpecialEvent(event.id).then(
                function () {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .textContent('התוכן נמחק בהצלחה')
                            .ok('סגור')
                    );

                    events.splice(events.indexOf(event), 1);
                },
                function () {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .textContent('התרחשה שגיאה בעת מחיקת התוכן')
                            .ok('סגור')
                    );
                });
        }

        function addEmptySpecialEvent(specialEvents) {
            var startDate   = new Date();
            var endDate     = new Date();

            specialEvents.push({ isNew: true, language: $scope.language.id, startDate, endDate, id: 0 });
        }

        function uploadPicture (picture, event) {
            if (!angular.isDefined(picture)) {
                return;
            }

            $scope.isLoading        = true;

            var uploadUrl           = 'specialEvents/upload';

            var fileUploadQuery     = fileUpload.uploadFileToUrl(picture, uploadUrl).then(
                (success)   => {
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