app.controller('zooContactInfoCtrl', ['$q', '$scope', '$mdDialog', 'utilitiesService', 'zooInfoService',
    function zooContactInfoController($q, $scope, $mdDialog, utilitiesService, zooInfoService) {
        $scope.isLoading            = true;

        initializeComponent();

        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];

                $scope.updateContactInfos($scope.language);
        });

        function initializeComponent() {
            $scope.updateContactInfos       = function (language) {
                $scope.language             = language;

                var contactInfoQuery        = zooInfoService.contactInfo.getAllContactInfos(language.id).then(
                    function (data) {
                        $scope.contactInfos = data.data;

                        addEmptyContactInfo($scope.contactInfos);
                    },
                    function () {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .textContent('אירעה שגיאה במהלך טעינת הנתונים')
                                .ok('סגור')
                        );
                    });

                var contactInfoNoteQuery    = zooInfoService.contactInfo.getContactInfoNote(language.id).then(
                    function (data) {
                        $scope.contactInfoNote = data.data[0] || { contactInfoNote: '' };
                    },
                    function () {
                        utilitiesService.utilities.alert("אירעה שגיאה במהלך טעינת הנתונים");
                    });

                var promises = [contactInfoQuery, contactInfoNoteQuery];

                $q.all(promises).then(
                    () => $scope.isLoading    = false,
                    () => $scope.isLoading    = false);
            }

            $scope.confirmDeleteContactInfo = function (ev, contactInfo, contactInfos) {
                var confirm = $mdDialog.confirm()
                    .title('האם אתה בטוח שברצונך למחוק את תוכן זה?')
                    .textContent('לאחר המחיקה, לא תוכל להחזירו אלא ליצור אותו מחדש')
                    .targetEvent(ev)
                    .ok('אישור')
                    .cancel('ביטול');

                $mdDialog.show(confirm).then(function () { deleteContactInfo(contactInfo, contactInfos); });
            }

            $scope.addContactInfo           = function (contactInfo) {
                if (!checkContactInfo(contactInfo)) {
                    return;
                }

                $scope.isLoading        = true;
                var successContent      = contactInfo.isNew ? 'האירוע נוסף בהצלחה!' : 'האירוע עודכן בהצלחה!';
                var failContent         = contactInfo.isNew ? 'התרחשה שגיאה בעת שמירת האירוע' : 'התרחשה שגיאה בעת עדכון האירוע';

                zooInfoService.contactInfo.updateContactInfo(contactInfo).then(
                    function () {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .textContent(successContent)
                                .ok('סגור')
                        );

                        $scope.isLoading = false;

                        $scope.updateContactInfos($scope.language);
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
            }

            $scope.addContactInfoNote       = function (contactInfoNote, languageId) {
                zooInfoService.contactInfo.updateContactInfoNote(contactInfoNote, languageId).then(
                    () => utilitiesService.utilities.alert('התוכן נשמר בהצלחה'),
                    () => utilitiesService.utilities.alert('אירעה שגיאה בעת שמירת התוכן'));
            }
        }

        function addEmptyContactInfo(contactInfos) {
            contactInfos.push({ isNew: true, language: $scope.language.id, id: 0 });
        }

        function deleteContactInfo(contactInfo, contactInfos) {
            zooInfoService.contactInfo.deleteContactInfo(contactInfo.id).then(
                function () {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .textContent('התוכן נמחק בהצלחה')
                            .ok('סגור')
                    );

                    contactInfos.splice(contactInfos.indexOf(contactInfo), 1);
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

        function checkContactInfo(contactInfo) {
            if (!contactInfo) {
                return;
            }

            if (!angular.isDefined(contactInfo.via) || contactInfo.via === '') {
                utilitiesService.utilities.alert('אנא הכנס דרך התקשרות');

                return false;
            }

            if (!angular.isDefined(contactInfo.address) || contactInfo.address === '') {
                utilitiesService.utilities.alert('אנא הכנס כתובת התקשרות');

                return false;
            }

            return true;
        }
}]);