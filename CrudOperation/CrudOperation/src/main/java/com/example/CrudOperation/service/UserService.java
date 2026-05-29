    package com.example.CrudOperation.service;

    import com.example.CrudOperation.entity.User;
    import com.example.CrudOperation.repository.UserRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.stereotype.Service;
    import org.springframework.web.server.ResponseStatusException;

    import java.util.List;

    @Service
    public class UserService {

        @Autowired
        private UserRepository userRepository;

        public User saveUser(User user) {
            return userRepository.save(user);
        }

        public List<User> getAllUsers() {
            return userRepository.findAll();
        }

        public User getUserById(Long id) {
            return userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
        }

        public User updateUser(Long id, User user) {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));

            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());

            return userRepository.save(existingUser);
        }

        public void deleteUser(Long id) {
            if (!userRepository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
            }

            userRepository.deleteById(id);
        }
    }
