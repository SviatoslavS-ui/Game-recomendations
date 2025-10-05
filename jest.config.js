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
  
  // Mock files for specific imports
  moduleNameMapper: {
    // If you have CSS imports in your JS files
    '\\.(css|less|scss|sass)$': '<rootDir>/src/test/javascript/mocks/styleMock.js'
  },
  
  // Coverage configuration
  collectCoverageFrom: [
    '<rootDir>/src/main/resources/static/js/**/*.js'
  ],
  
  // Transform files
  transform: {
    '^.+\\.js$': 'babel-jest'
  },
  
  // Don't transform node_modules
  transformIgnorePatterns: [
    '/node_modules/'
  ],
  
  // Test environment setup
  testEnvironmentOptions: {
    customExportConditions: ['node', 'node-addons']
  },
  
  // Verbose output
  verbose: true
};
