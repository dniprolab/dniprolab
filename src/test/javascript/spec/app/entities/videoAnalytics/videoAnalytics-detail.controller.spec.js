'use strict';

describe('Controller Tests', function() {

    describe('VideoAnalytics Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockVideoAnalytics, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockVideoAnalytics = jasmine.createSpy('MockVideoAnalytics');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'VideoAnalytics': MockVideoAnalytics,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("VideoAnalyticsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'dniprolabApp:videoAnalyticsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
