var myApp = angular.module('directoryWatcher', []);

myApp.controller('MenuController', ['$scope','$rootScope', function($scope, $rootScope) {

    $scope.closeWatchers = function(){
        $rootScope.$emit("Disconnect", {});

        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/app/stop',
            crossDomain: true,
            success: function(data) {
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
        $scope.$destroy();
    });

    $scope.event=""

    $scope.startWatching = function(path){
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/app/start',
            crossDomain: true,
            contentType: "application/text; charset=UTF-8",
            data: path,
            dataType: 'json',
            success: function(data) {
                $scope.connect(data);
            },
            error: function(){
                alert("error")
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
        console.log("Disconnected");
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
        scope: {
            onRemove:"&"
        },
        link: function($scope, $element, $attrs, $transclude) {
            $scope.remove = function() {
                $element.remove();
                $scope.$destroy();
            }
        },
        templateUrl: 'watcherDirective.html'
    };
});