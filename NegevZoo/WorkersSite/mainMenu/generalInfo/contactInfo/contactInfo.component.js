app.controller('zooContactInfoCtrl', ['$q', '$scope', 'utilitiesService', 'zooInfoService',
    function zooContactInfoController($q, $scope, utilitiesService, zooInfoService) {
        $scope.isLoading            = true;

        // Initialize the component.
        initializeComponent();

        // Get the languages.
        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];

                // Update the contact infos.
                $scope.updateContactInfos($scope.language);
        });

        // Initializes the component.
        function initializeComponent() {
            // Initialize the update contact infos function.
            $scope.updateContactInfos       = function (language) {
                $scope.language             = language;

                // Get the contact infos.
                var contactInfoQuery        = zooInfoService.contactInfo.getAllContactInfos(language.id).then(
                    function (data) {
                        $scope.contactInfos = data.data;

                        // Add an empty contact info for the user to add.
                        addEmptyContactInfo($scope.contactInfos);
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                    });

                // Get the contact info note.
                var contactInfoNoteQuery    = zooInfoService.contactInfo.getContactInfoNote(language.id).then(
                    function (data) {
                        $scope.contactInfoNote = data.data || { contactInfoNote: '' };
                    },
                    function () {
                        utilitiesService.utilities.alert("אירעה שגיאה במהלך טעינת הנתונים");
                    });

                var promises = [contactInfoQuery, contactInfoNoteQuery];

                $q.all(promises).then(
                    () => $scope.isLoading    = false,
                    () => $scope.isLoading    = false);
            }

            // Initialize the confirm delete contact info function.
            $scope.confirmDeleteContactInfo = function (ev, contactInfo, contactInfos) {
                utilitiesService.utilities.confirm({ title: 'מחיקת דרך יצירת קשר', text: 'האם אתה בטוח שברצונך למחוק דרך יצירת קשר זו?' }).then(
                    function () {
                        deleteContactInfo(contactInfo, contactInfos);
                    });

                return;
            }

            // Initialize the add contact info function.
            $scope.addContactInfo           = function (contactInfo) {
                if (!checkContactInfo(contactInfo)) {
                    return;
                }

                $scope.isLoading        = true;

                // Initialize the return statement.
                var successContent      = contactInfo.isNew ? 'דרך ההתקשרות נוספה בהצלחה!' : 'דרך ההתקשרות עודכנה בהצלחה!';
                var failContent         = contactInfo.isNew ? 'התרחשה שגיאה בעת שמירת דרך ההתקשרות' : 'התרחשה שגיאה בעת עדכון דרך ההתקשרות';

                // Update the contact info.
                zooInfoService.contactInfo.updateContactInfo(contactInfo).then(
                    function () {
                        utilitiesService.utilities.alert(successContent);

                        $scope.isLoading = false;

                        $scope.updateContactInfos($scope.language);
                    },
                    function () {
                        utilitiesService.utilities.alert(failContent);

                        $scope.isLoading = false;
                    });
            }

            // Initialize the add contact info note function.
            $scope.addContactInfoNote       = function (contactInfoNote, languageId) {
                var contactInfoObj          = { contactInfoNote: contactInfoNote };

                zooInfoService.contactInfo.updateContactInfoNote(contactInfoObj, languageId).then(
                    () => utilitiesService.utilities.alert('התוכן נשמר בהצלחה'),
                    () => utilitiesService.utilities.alert('אירעה שגיאה בעת שמירת התוכן'));
            }
        }

        // Adds an empty contact info.
        function addEmptyContactInfo(contactInfos) {
            contactInfos.push({ isNew: true, language: $scope.language.id, id: 0 });
        }

        // Deletes a contact info.
        function deleteContactInfo(contactInfo, contactInfos) {
            zooInfoService.contactInfo.deleteContactInfo(contactInfo.id).then(
                function () {
                    utilitiesService.utilities.alert('דרך יצירת הקשר נמחקה בהצלחה');

                    // Remove the contact info from the contact infos array.
                    contactInfos.splice(contactInfos.indexOf(contactInfo), 1);
                },
                function () {
                    utilitiesService.utilities.alert('התרחשה שגיאה בעת מחיקת דרך יצירת הקשר');
                });
        }

        // Checks the contact info's validity.
        function checkContactInfo(contactInfo) {
            // If no contact info was given, return.
            if (!contactInfo) {
                return;
            }

            // If no via was entered, return.
            if (!angular.isDefined(contactInfo.via) || contactInfo.via === '') {
                utilitiesService.utilities.alert('אנא הכנס דרך התקשרות');

                return false;
            }

            // If no address was entered, return.
            if (!angular.isDefined(contactInfo.address) || contactInfo.address === '') {
                utilitiesService.utilities.alert('אנא הכנס כתובת התקשרות');

                return false;
            }

            return true;
        }
}]);