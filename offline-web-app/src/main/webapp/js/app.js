
var app = angular.module('comalatApp', ['ngRoute', 'ngCookies']);

app.config(function ($routeProvider) {
    $routeProvider.
            when('/comalat', {
                templateUrl: 'partials/comalat.html',
                controller: 'APICtrl'
            }).
            when('/about', {
                templateUrl: 'partials/about.html'
            }).
            when('/login', {
                templateUrl: 'partials/login.html',
                controller: 'loginCtrl'
            }).
            otherwise({redirectTo: '/login'});
});

app.directive('fileModel', ['$parse', function ($parse) {
        return {
            restict: 'A',
            link: function (scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;

                element.bind('change', function () {
                    scope.$apply(function () {
                        modelSetter(scope, element[0].files[0]);
                    });
                });


            }
        };

    }]);

app.directive('ngConfirmClick', [
    function () {
        return {
            link: function (scope, element, attr) {
                var msg = attr.ngConfirmClick || "Are you sure?";
                var clickAction = attr.confirmedClick;
                element.bind('click', function (event) {
                    if (window.confirm(msg)) {
                        scope.$eval(clickAction)
                    }
                });
            }
        };
    }]);

app.filter('removeSpaces', [function () {
        return function (string) {
            if (!angular.isString(string)) {
                return string;
            }
            return string.replace(/[\s]/g, '');
        };
    }]);

app.filter('KiloByte', [function () {
        return function (size) {
            var kb = 1024;
            return Math.round(size / kb);
        };
    }]);

app.filter('MegaByte', [function () {
        return function (size) {
            var mb = 1048576;
            return Math.round((size / mb) * 100) / 100;
        };
    }]);

app.controller('mainCtrl', function ($rootScope, $scope, $location, $timeout, AuthorizationService) {

    $rootScope.logout = function () {
        AuthorizationService.ClearCredentials();
        $location.path('/login');
    };

    $scope.userModal = function () {
        $scope.usermodalfullname = $rootScope.user.fullname;
        $scope.usermodalname = $rootScope.user.username;
        $scope.usermodalpass = null;
        $scope.usermodalpassconf = null;
        $scope.userform.usermodalpass.$dirty = false;
        $scope.userform.usermodalpassconf.$dirty = false;
        $scope.dataloading = false;
    };

    $rootScope.login = function () {
        $location.path('/login');
    };

    $rootScope.edit = function () {
        $scope.dataloading = true;
        var res = AuthorizationService.Edit($scope.usermodalname, $scope.usermodalpass, $scope.usermodalfullname);
        res.then(
                function (response) {
                    if (response.status !== 200) {
                        //console.log("Edit error: " + response.data.message);
                        if (response.data.message !== undefined) {
                            displayMessage(response.data.message, response.status, response.data.documentation, "error");
                        } else {
                            displayMessage("Something goes wrong!", response.status, "If this error insist contact us!", "error");
                        }
                        $scope.dataloading = false;
                        angular.element('#UserModal').modal('hide');
                    } else {
                        var un = $scope.usermodalname;
                        var pass = $scope.usermodalpass;
                        var fn = $scope.usermodalfullname;
                        AuthorizationService.SetCredentials(un, pass, fn);
                        $scope.dataloading = false;
                        angular.element('#UserModal').modal('hide');
                        if (response.data.message !== undefined) {
                            displayMessage(response.data.message, response.status, response.data.documentation, "info");
                        } else {
                            displayMessage("Updated Done!", response.status, null, "info");
                        }
                    }
                }
        );
    };

    var displayMessage = function (message, code, doc, type) {
        if (type === "info") {
            $rootScope.info.message = message;
            $rootScope.info.code = code;
            $rootScope.info.flag = true;
            $rootScope.info.documentation = doc;
            $timeout(function () {
                $rootScope.info.flag = false;
            }, 3000);
        } else {
            $rootScope.error.message = message;
            $rootScope.error.code = code;
            $rootScope.error.flag = true;
            $rootScope.error.documentation = doc;
            $timeout(function () {
                $rootScope.error.flag = false;
            }, 5000);
        }
    };
});

app.controller('loginCtrl', function ($location, $rootScope, $scope, AuthorizationService) {
    $scope.error = false;
    if ($rootScope.user.authdata !== undefined) {
        $location.path('/comalat');
    }

    $scope.submit = function () {
        $scope.dataLoading = true;

        var res = AuthorizationService.Login($scope.username, $scope.password);
        res.then(
                function (response) {
                    if (response.status !== 200) {
                        //console.log("Login error: " + response.data.message);
                        $scope.errorMessage = response.data.message;
                        $scope.error = true;
                        $scope.dataLoading = false;
                    } else {
                        var un = response.data.username;
                        var pass = response.data.password;
                        var fn = response.data.fullname;
                        AuthorizationService.SetCredentials(un, pass, fn);
                        $location.path('/comalat');
                    }
                }
        );
    };
});


app.controller('APICtrl', ['$scope', '$rootScope', '$timeout', '$http', 'getData', 'uploadFile', 'deleteFile',
    function ($scope, $rootScope, $timeout, $http, getData, uploadFile, deleteFile) {
        $rootScope.info = {
            flag: false,
            message: '',
            code: '',
            documentation: ''
        };
        $rootScope.error = {
            flag: false,
            message: '',
            code: 0,
            documentation: ''
        };
        var uploadURL = null;
        $scope.largeView = 6;
        var resetForm = function () {
            $scope.action_method = null;
            $scope.name = null;
            $scope.objName = null;
            $scope.uploadfile = null;
            angular.element('#uploadfile').val('');
            $scope.form.name.$dirty = false;
            uploadURL = null;
        };

        var init = $scope.init = function () {
            $scope.dataloading = true;
            $scope.show = false;
            $scope.selectedLang = null;
            $scope.unitcounter = 0;
            $scope.largeView = 6;

            var res = getData.getDatafromServer();
            res.then(
                    function (response) {
                        if (response.status === 200) {
                            //console.log("GET DATA OK");
                            $scope.languages = response.data.Languages;
                            if (response.data.Languages.length < 1) {
                                displayMessage("There is no Language lesson!", response.status, null, "info");
                            }

                            if ($scope.languages.length > 1) {
                                $scope.largeView = 4;
                            }
                            $scope.dataloading = false;
                        } else {
                            if (response.data.message !== undefined) {
                                displayMessage(response.data.message, response.status, response.data.documentation, "error");
                            } else {
                                displayMessage("Something goes wrong!", response.status, "If this error insist contact us!", "error");
                            }
                            $scope.dataloading = false;
                        }
                    }
            );
        };

        $scope.show_hide = function (selectedLang) {
            if ($scope.show === false) {
                $scope.show = true;
                $scope.selectedLang = selectedLang;
            } else {
                if ($scope.selectedLang === selectedLang) {
                    $scope.show = false;
                    $scope.selectedLang = null;
                } else {
                    $scope.selectedLang = selectedLang;
                }
            }
        };

        // GET methods
        $scope.getMethod = function (lang, lvl, course, unit) {
            $scope.dataloading = true;
            //console.log("getMethod: lang= " + lang + " lvl= " + lvl + " course= " + course + " unit= " + unit);
            if (lang !== null && lvl === null) {
                getCall('comalat/languages/', lang);
            } else if (lang !== null && lvl !== null && course === null) {
                getCall('comalat/languages/' + lang + '/levels/', lvl);
            } else if (lang !== null && lvl !== null && course !== null && unit === null) {
                getCall('comalat/languages/' + lang + '/levels/' + lvl + '/courses/', course);
            } else if (lang !== null && lvl !== null && course !== null && unit !== null) {
                getCall('comalat/languages/' + lang + '/levels/' + lvl + '/courses/' + course + '/units/', unit);
            }
        };

        var getCall = function (url, name) {
            var res = getData.getFilefromServer(url, name);
            res.then(
                    function (response) {
                        if (response.status !== 200) {
                            //console.log("GET " + name + " FILE ERROR: " + response.data.message);
                            if (response.data.message !== undefined) {
                                displayMessage(response.data.message, response.status, response.data.documentation, "error");
                            } else {
                                displayMessage("Something goes wrong!", response.status, "If this error insist contact us!", "error");
                            }
                        }
                        $scope.dataloading = false;
                    }
            );

        };

        // DELETE methods
        $scope.deleteMethod = function (lang, lvl, course, unit) {
            $scope.dataloading = true;
            //console.log("deleteMethod: lang= " + lang + " lvl= " + lvl + " course= " + course + " unit= " + unit);
            if (lang !== null && lvl === null) {
                deleteCall('comalat/languages/', lang);
            } else if (lang !== null && lvl !== null && course === null) {
                deleteCall('comalat/languages/' + lang + '/levels/', lvl);
            } else if (lang !== null && lvl !== null && course !== null && unit === null) {
                deleteCall('comalat/languages/' + lang + '/levels/' + lvl + '/courses/', course);
            } else if (lang !== null && lvl !== null && course !== null && unit !== null) {
                deleteCall('comalat/languages/' + lang + '/levels/' + lvl + '/courses/' + course + '/units/', unit);
            }
        };

        var deleteCall = function (url, name) {
            var res = deleteFile.deleteFilefromServer(url, name);
            res.then(
                    function (response) {
                        //console.log("deleteMethod: " + name + " status: " + response.status);
                        if (response.status !== 200) {
                            if (response.data.message !== undefined) {
                                displayMessage(response.data.message, response.status, response.data.documentation, "error");
                            } else {
                                displayMessage("DELETE FAIL", response.status, "If this error insist contact us!", "error");
                            }
                        } else {
                            if (response.data.message !== undefined) {
                                displayMessage(response.data.message, response.status, response.data.documentation, "info");
                            } else {
                                displayMessage("DELETE DONE", response.status, null, "info");
                            }
                            init();
                        }
                        $scope.dataloading = false;
                    }
            );
        };

        // POST methods
        $scope.postMethod = function (lang, lvl, course) {
            resetForm();
            //console.log("postMethod: lang= " + lang + " lvl= " + lvl + " course= " + course);
            $scope.format = '.zip';
            $scope.action_method = "Upload";
            if (lang === null) {
                //console.log("Post new Language");
                $scope.objName = "Language";
                uploadURL = 'comalat/languages/';
            } else if (lang !== null && lvl === null) {
                //console.log("Post new Education Level");
                $scope.objName = "Education Level";
                uploadURL = 'comalat/languages/' + lang + '/levels/';
            } else if (lang !== null && lvl !== null && course === null) {
                //console.log("Post new Course");
                $scope.objName = "Course";
                uploadURL = 'comalat/languages/' + lang + '/levels/' + lvl + '/courses/';
            } else if (lang !== null && lvl !== null && course !== null) {
                //console.log("Post new Unit");
                $scope.objName = "Unit";
                $scope.format = '.pdf';
                uploadURL = 'comalat/languages/' + lang + '/levels/' + lvl + '/courses/' + course + '/units/';
            }
        };


        // PUT methods
        $scope.putMethod = function (lang, lvl, course, unit) {
            resetForm();
            $scope.action_method = "Update";
            $scope.format = '.zip';
            //console.log("putMethod: lang= " + lang + " lvl= " + lvl + " course= " + course + " unit= " + unit);
            if (lang !== null && lvl === null) {
                //console.log("putMethod: Language = " + lang);
                $scope.name = lang;
                $scope.objName = "Language";
                uploadURL = 'comalat/languages/';
            } else if (lang !== null && lvl !== null && course === null) {
                //console.log("putMethod: Level = " + lvl);
                $scope.name = lvl;
                $scope.objName = "Education Level";
                uploadURL = 'comalat/languages/' + lang + '/levels/';
            } else if (lang !== null && lvl !== null && course !== null && unit === null) {
                //console.log("putMethod: Courses = " + course);
                $scope.name = course;
                $scope.objName = "Course";
                uploadURL = 'comalat/languages/' + lang + '/levels/' + lvl + '/courses/';
            } else if (lang !== null && lvl !== null && course !== null && unit !== null) {
                //console.log("putMethod: Units = " + unit);
                $scope.name = unit;
                $scope.objName = "Unit";
                $scope.format = '.pdf';
                uploadURL = 'comalat/languages/' + lang + '/levels/' + lvl + '/courses/' + course + '/units/';
            }
        };

        // submit upload update
        $scope.submit = function () {
            //console.log("Submit");
            $scope.datamodalloading = true;
            var file = $scope.uploadfile;
            var name = $scope.name;
            if (file !== null) {
                if ($scope.action_method === "Upload") {
                    //console.log("Upload");
                    var res = uploadFile.uploadFiletoServer(file, name, uploadURL);
                    res.then(
                            function (response) {
                                //console.log("UploadMethod: DONE: " + response.status + "  " + response.data.message);
                                angular.element('#myModal').modal('hide');
                                $scope.datamodalloading = false;
                                if (response.status !== 201) {
                                    if (response.data.message !== undefined) {
                                        displayMessage(response.data.message, response.status, response.data.documentation, "error");
                                    } else {
                                        displayMessage("UPLOAD FAIL", response.status, "If this error insist contact us!", "error");
                                    }
                                } else {
                                    if (response.data.message !== undefined) {
                                        displayMessage(response.data.message, response.status, response.data.documentation, "info");
                                    } else {
                                        displayMessage("UPLOAD DONE", response.status, null, "info");
                                    }
                                    init();
                                }
                            }
                    );
                } else if ($scope.action_method === "Update") {
                    //console.log("Update");
                    var res = uploadFile.updateFiletoServer(file, name, uploadURL);
                    res.then(
                            function (response) {
                                ///console.log("UpdateMethod: DONE: " + response.status + "  " + response.data.message);
                                angular.element('#myModal').modal('hide');
                                $scope.datamodalloading = false;
                                if (response.status !== 200) {
                                    if (response.data.message !== undefined) {
                                        displayMessage(response.data.message, response.status, response.data.documentation, "error");
                                    } else {
                                        displayMessage("UPDATE FAIL", response.status, "If this error insist contact us!", "error");
                                    }
                                } else {
                                    if (response.data.message !== undefined) {
                                        displayMessage(response.data.message, response.status, response.data.documentation, "info");
                                    } else {
                                        displayMessage("UPDATE DONE", response.status, null, "info");
                                    }
                                    init();
                                }
                            }
                    );
                }
            }
        };

        var displayMessage = function (message, code, doc, type) {
            if (type === "info") {
                $scope.info.message = message;
                $scope.info.code = code;
                $scope.info.flag = true;
                $scope.info.documentation = doc;
                $timeout(function () {
                    $scope.info.flag = false;
                }, 3000);
            } else {
                $scope.error.message = message;
                $scope.error.code = code;
                $scope.error.flag = true;
                $scope.error.documentation = doc;
                $timeout(function () {
                    $scope.error.flag = false;
                }, 5000);
            }
        };
    }]);