'use strict';

angular.module('dniprolabApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('videoAnalyticsWidget', {
                parent: 'entity',
                url: '/videoAnalyticsWidget',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'dniprolabApp.videoAnalytics.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/videoAnalytics/videoAnalyticsWidget.html',
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
            })
            .state('videoAnalyticsWidget.all', {
                parent: 'entity',
                url: '/videoAnalyticsWidget/all',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'dniprolabApp.videoAnalytics.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/videoAnalytics/videoAnalyticsWidget-all.html',
                        controller: 'VideoAnalyticsWidgetAllController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('videoAnalytics');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'VideoAnalytics', function ($stateParams, VideoAnalytics) {
                        return VideoAnalytics.get({id: $stateParams.id});
                    }]
                }
            });
    });
