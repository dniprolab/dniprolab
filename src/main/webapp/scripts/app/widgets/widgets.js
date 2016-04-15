'use strict'

angular.module('dniprolabApp')
    .directive('schedulesWidget', function(){
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/widgets/schedules-widget.html',
            controller: 'ScheduleController'
        };
    }).directive('messagesWidget', function(){
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/widgets/messages-widget.html',
            controller: 'MessageController'
        };
    }).directive('analyticsWidget', function(){
        return {
            restrict: 'E',
            templateUrl: 'scripts/app/widgets/analytics-widget.html',
            controller: 'VideoAnalyticsWidgetController'
        };
    });
