module.exports = {
  // The root directory that Jest should scan for tests
  rootDir: '.',
  
  // The test environment that will be used for testing
  testEnvironment: 'jsdom',
  
  // The glob patterns Jest uses to detect test files
  testMatch: [
    '<rootDir>/src/test/javascript/**/*.test.js'
  ],
  
  // An array of file extensions your modules use
  moduleFileExtensions: ['js', 'json'],
  
  // Setup files to run before each test
  setupFilesAfterEnv: ['<rootDir>/src/test/javascript/setup/setupTests.js'],
  
  // Mock files for specific imports
  moduleNameMapper: {
    // If you have CSS imports in your JS files
    '\\.(css|less|scss|sass)$': '<rootDir>/src/test/javascript/mocks/styleMock.js'
  },
  
  // Coverage configuration
  collectCoverageFrom: [
    '<rootDir>/src/main/resources/static/js/**/*.js'
  ],
  
  // Verbose output
  verbose: true
};
