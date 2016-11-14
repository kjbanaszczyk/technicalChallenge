var myApp = angular.module('directoryWatcher', []);

myApp.controller('MenuController', ['$scope','$rootScope', function($scope, $rootScope) {

    $scope.closeWatchers = function(){
        $rootScope.$emit("Disconnect", {});
    };


    $scope.destroySession = function(){
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/app/endSession',
            crossDomain: true,
            xhrFields: {
               withCredentials: true
            },
            success: function(data) {
                console.log("Success")
            },
            error: function(){
                alert("error")
            }
        });
    };

}]);

myApp.controller('ConnectionController', ['$scope', '$rootScope', function($scope, $rootScope) {
    var stompClient = null;

    $rootScope.$on("Disconnect", function(){
           $scope.disconnect();
           $scope.remove();
    });

    $scope.$on("$destroy", function(){
        $scope.disconnect();
    });

    $scope.event=""

    $scope.websocket=""

    $scope.startWatching = function(path){
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/app/start',
            crossDomain: true,
            contentType: "application/text; charset=UTF-8",
            data: path,
            xhrFields: {
               withCredentials: true
            },
            dataType: 'text',
            success: function(data) {
                console.log("test1");
                $scope.websocket=data;
                console.log("test2");
                $scope.connect(data);
            },
            error: function(data){
                alert(data.toString())
            }});
    }

    $scope.connect = function(endPoint) {
        var socket = new SockJS('http://localhost:8080/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/events/get/'+endPoint, function (event) {
                $scope.$apply(function () {
                        $scope.event = JSON.parse(event.body).path + ": " +
                          JSON.parse(event.body).eventType;
                });
            });
        });
    }

   $scope.disconnect = function() {
        if (stompClient != null) {
            stompClient.disconnect();
            stompClient = null;
        }
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/app/stop/' + $scope.websocket,
            crossDomain: true,
            xhrFields: {
               withCredentials: true
            },
            success: function(data) {
                console.log("disconnected from " + $scope.websocket)
            },
            error: function(){
                alert("error")
            }});
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
        restrict: 'E',
        scope: {
            event: '='
        },
        link: function(scope, element) {
            scope.$watch('event', function(newValue, oldValue) {
                if(newValue){
                    element.append("<tr><td>" + newValue + "</td></tr>");
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