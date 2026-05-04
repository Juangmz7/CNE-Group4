import { render, screen } from '@testing-library/react';
import App from './App';

test('unauthenticated home page redirects to sign in', () => {
    render(<App />);
    expect(screen.getByText(/sign in/i)).toBeInTheDocument();
});