app.controller('zooContactInfoCtrl', ['$scope', '$mdDialog', 'zooInfoService',
    function zooContactInfoController($scope, $mdDialog, zooInfoService) {
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
                $scope.language         = language;

                zooInfoService.contactInfo.getAllContactInfos(language.id).then(
                    function (data) {
                        $scope.contactInfos = data.data;
                        $scope.isLoading    = false;

                        addEmptyContactInfo($scope.contactInfos);
                    },
                    function () {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .textContent('אירעה שגיאה במהלך טעינת הנתונים')
                                .ok('סגור')
                        );

                        $scope.isLoading    = false;
                    });
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
}]);