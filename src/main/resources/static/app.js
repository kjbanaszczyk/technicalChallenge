var myApp = angular.module('directoryWatcher', []);
var myHost = "http://" + window.location.host;

myApp.controller('MenuController', ['$scope','$rootScope','$timeout', '$http', function($scope, $rootScope, $timeout, $http) {

    $scope.closeWatchers = function(){
        $rootScope.$emit("Disconnect", {});
    };

    $scope.label = "";
    $scope.hideLabel = true;

    $scope.showLabelFun = function (jqXHR) {
         $scope.hideLabel=false;
         $timeout(function () {$scope.hideLabel = true}, 1000);
    };

    $scope.addFile = function(path){
     $http({
        method: 'POST',
        url: myHost + '/addFile',
        data: path,
        withCredentials: true
        }).then(function successCallback(response) {
                $scope.label=response.data;
                $scope.hideLabel=false;
                $timeout(function () {$scope.hideLabel = true}, 3000);
             }, function errorCallback(response) {
                $scope.label=response.data;
                $scope.hideLabel=false;
                $timeout(function () {$scope.hideLabel = true}, 3000);
             });
    }

    $scope.removeFile = function(path){
         $http({
            method: 'POST',
            url: myHost + '/removeFile',
            data: path,
            withCredentials: true
            }).then(function successCallback(response) {
                    $scope.label=response.data;
                    $scope.hideLabel=false;
                    $timeout(function () {$scope.hideLabel = true}, 3000);
                 }, function errorCallback(response) {
                    $scope.label=response.data;
                    $scope.hideLabel=false;
                    $timeout(function () {$scope.hideLabel = true}, 3000);
                 });
        }

    $scope.destroySession = function(){

        $http({
        method: 'POST',
        url: myHost + '/app/endSession',
        data: '',
        withCredentials: true
        }).then(function successCallback(response) {
                console.log("Success")
             }, function errorCallback(response) {
                $scope.label=response.data;
                $scope.hideLabel=false;
                $timeout(function () {$scope.hideLabel = true}, 3000);
             });
    };

}]);

myApp.controller('ConnectionController', ['$scope', '$rootScope', '$http', '$timeout', function($scope, $rootScope, $http, $timeout) {
    var stompClient = null;

    $rootScope.$on("Disconnect", function(){
           $scope.disconnect();
           $scope.remove();
    });

    $scope.$on("$destroy", function(){
        $scope.disconnect();
    });

    $scope.label = "";

    $scope.event="";

    $scope.websocket="";

    $scope.obtainEndPoint = function(path){
        $http({
                method: 'GET',
                url: myHost + '/app/obtainEndPoint',
                withCredentials: true
                }).then(function successCallback(response) {
                            $scope.connect(response.data, $scope.startWatching, path, response.data);
                         }, function errorCallback(response) {
                            $scope.label=response.data;
                            console.log(response.toString());
                            $scope.hideLabel=false;
                            $timeout(function () {$scope.hideLabel = true}, 1000);
                         });
    }

    $scope.startWatching = function(path, endPoint){
        $http({
        method: 'POST',
        url: myHost + '/app/start/'+endPoint,
        data: path,
        withCredentials: true
        }).then(function successCallback(response) {
                    $scope.websocket=endPoint;
                 }, function errorCallback(response) {
                    $scope.label=response.data;
                    console.log(response.toString());
                    $scope.hideLabel=false;
                    $timeout(function () {$scope.hideLabel = true}, 1000);
                 });
    }

    $scope.connect = function(endPoint, callback, path, response) {
        var socket = new SockJS(myHost + '/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/events/get/'+endPoint, function (event) {
                $scope.$apply(function () {
                        $scope.event = "<tr><td style=\"padding: 5px;\">" + JSON.parse(event.body).path + "</td><td style=\"padding: 5px;\">" +
                          JSON.parse(event.body).eventType + "</td></tr>";
                });
            });
            callback(path, response);
        });
    }

   $scope.disconnect = function() {
        if (stompClient != null) {
            stompClient.disconnect();
            stompClient = null;
        }

        $http({
        method: 'POST',
        url: myHost + '/app/stop/' + $scope.websocket,
        data: '',
        withCredentials: true
        }).then(function successCallback(response) {
                    console.log("disconnected from " + $scope.websocket)
                 }, function errorCallback(response) {
                    $scope.label=response.data;
                    $scope.hideLabel=false;
                    $timeout(function () {$scope.hideLabel = true}, 1000);
                 });
    }

}]);

myApp.directive("addWatcher", function($compile){
	return function(scope, element, attrs){
		element.bind("click", function(){
			angular.element(document.getElementById('watchers-space')).append($compile("<Watcher></Watcher>")(scope));
		});
	};
});

myApp.directive('listenerEventsDirective', function() {
    return{
        restrict: 'A',
        scope: {
            event: '='
        },
        link: function(scope, element) {
            scope.$watch('event', function(newValue, oldValue) {
                if(newValue){
                    element.append(newValue);
                }
            });
        }
    }
});

myApp.directive('watcher', function() {
    return {
        scope: true,
        link: function($scope, $element, $attrs, $transclude) {
            $scope.remove = function() {
                $element.remove();
                $scope.$destroy();
            }
        },
        templateUrl: 'watcherDirective.html'
    };
});