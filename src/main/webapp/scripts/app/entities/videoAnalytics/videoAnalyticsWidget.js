'use strict';

angular.module('dniprolabApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('videoAnalyticsWidget', {
                parent: 'entity',
                url: '/videoAnalyticssWidget',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'dniprolabApp.videoAnalytics.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/videoAnalytics/videoAnalyticssWidget.html',
                        controller: 'VideoAnalyticsWidgetController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('videoAnalytics');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    });
