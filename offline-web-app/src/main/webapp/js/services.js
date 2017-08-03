/*** Global ***/
var baseURI = 'http://localhost:8080/offline-web-app/';

app.factory('AuthorizationService', ['Base64', '$cookieStore', '$rootScope', '$http',
    function (Base64, $cookieStore, $rootScope, $http) {
        var service = {};
        var fullname = "";
        service.Login = function (username, password, callback) {
            var response = {};
            // Testing login
            if (username === "admin" && password === "admin") {
                fullname = "Admin";
                response = {
                    message: "Login Success",
                    code: 200,
                    documentation: null
                };
            } else {
                response = {
                    message: "Username or password is incorrect",
                    code: 404,
                    documentation: null
                };
            }
            callback(response);
        };

        service.SetCredentials = function (username, password) {
            var authdata = Base64.encode(username + ':' + password);
            $rootScope.IsloggedIn = true;
            $rootScope.user = {
                username: username,
                authdata: authdata,
                fullname: fullname
            };

            $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata;
            $cookieStore.put('comalatUSER', $rootScope.user);
        };

        service.ClearCredentials = function () {
            $rootScope.IsloggedIn = false;
            $rootScope.user = {};
            $cookieStore.remove('comalatUSER');
            delete $http.defaults.headers.common.Authorization;
        };

        return service;
    }]);

app.service('getData', ['$http', function ($http) {
        this.getDatafromServer = function () {
            return $http.get(baseURI + 'comalat/data').then(
                    function (response) {
                        console.log("GET DATA SERVICE OK");
                        return response;
                    },
                    function (response) {
                        console.log("GET DATA SERVICE ERROR");
                        return response;
                    });
        };

        this.getFilefromServer = function (url, name) {
            var config = {
                responseType: "arraybuffer",
                transformResponse: jsonBufferToObject
            };
            return $http.get(baseURI + url + name, config).then(
                    function (response) {
                        console.log("GET FILE SERVICE OK");
                        success(response, name);
                        return response;
                    },
                    function (response) {
                        console.log("GET FILE SERVICE ERROR");
                        error(response);
                        return response;
                    });
        };

    }]);

app.service('deleteFile', ['$http', function ($http) {
        this.deleteFilefromServer = function (url, name) {
            return $http.delete(baseURI + url + name).then(
                    function (response) {
                        console.log("DELETE DATA SERVICE OK");
                        return response;
                    },
                    function (response) {
                        console.log("DELETE DATA SERVICE ERROR");
                        return response;
                    });
        }
    }]);

app.service('uploadFile', ['$http', function ($http) {

        this.uploadFiletoServer = function (file, name, url) {
            var fd = new FormData();

            fd.append('uploadFile', file);
            fd.append('name', name);

            return $http.post(baseURI + url + 'upload', fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            }).then(
                    function (response) {
                        console.log("UPLOAD SERVICE OK");
                        return response;
                    },
                    function (response) {
                        console.log("UPLOAD SERVICE ERROR");
                        return response;
                    });
        };

        this.updateFiletoServer = function (file, name, url) {
            var fd = new FormData();

            fd.append('uploadFile', file);
            fd.append('name', name);

            return $http.put(baseURI + url + 'update', fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            }).then(
                    function (response) {
                        console.log("UPDATE SERVICE OK");
                        return response;
                    },
                    function (response) {
                        console.log("UPDATE SERVICE ERROR");
                        return response;
                    });
        };

    }]);

app.run(['$rootScope', '$location', '$cookieStore', '$http',
    function ($rootScope, $location, $cookieStore, $http) {
        $rootScope.user = $cookieStore.get('comalatUSER') || {};
        if ($rootScope.user.authdata !== undefined) {
            $rootScope.IsloggedIn = true;
            console.log("MPIKA run: " + $rootScope.user.username + "  " + $rootScope.user.fullname + "  " + $rootScope.user.authdata);
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.user.authdata;
        }
    }]);

var success = function (response, name) {
    {
        console.log("OK " + response.headers('Content-Type'));
        var contentType = response.headers('Content-Type');
        var zipfilename = response.headers('x-zipfilename');
        var pdffilename = response.headers('x-pdffilename');
        var format = response.headers('x-fileformat');
        var linkElement = document.createElement('a');

        if (pdffilename !== null) {
            name = pdffilename;
        } else {
            name = name + format;
        }

        try {
            var blob = new Blob([response.data], {type: contentType});
            var win = (window.URL || window.webkitURL)
            var url = win.createObjectURL(blob);
            linkElement.setAttribute('href', url);
            linkElement.setAttribute("download", name);

            var clickEvent = new MouseEvent("click", {
                "view": window,
                "bubbles": true,
                "cancelable": true
            });

            linkElement.dispatchEvent(clickEvent);

        } catch (ex) {
            console.log(ex);
        }
    }
};

var error = function (response) {
    console.log("Error: " + response.data.message);
};


function jsonBufferToObject(data, headersGetter, status) {
    var type = headersGetter("Content-Type");
    if (!type.startsWith("application/json")) {
        return data;
    }

    var decoder = new TextDecoder("utf-8");
    var domString = decoder.decode(data);
    var json = JSON.parse(domString);
    return json;
}

app.factory('Base64', function () {
    /* jshint ignore:start */

    var keyStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';

    return {
        encode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            do {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);

                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;

                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }

                output = output +
                        keyStr.charAt(enc1) +
                        keyStr.charAt(enc2) +
                        keyStr.charAt(enc3) +
                        keyStr.charAt(enc4);
                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";
            } while (i < input.length);

            return output;
        },

        decode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
            var base64test = /[^A-Za-z0-9\+\/\=]/g;
            if (base64test.exec(input)) {
                window.alert("There were invalid base64 characters in the input text.\n" +
                        "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
                        "Expect errors in decoding.");
            }
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

            do {
                enc1 = keyStr.indexOf(input.charAt(i++));
                enc2 = keyStr.indexOf(input.charAt(i++));
                enc3 = keyStr.indexOf(input.charAt(i++));
                enc4 = keyStr.indexOf(input.charAt(i++));

                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;

                output = output + String.fromCharCode(chr1);

                if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
                }

                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";

            } while (i < input.length);

            return output;
        }
    };
});