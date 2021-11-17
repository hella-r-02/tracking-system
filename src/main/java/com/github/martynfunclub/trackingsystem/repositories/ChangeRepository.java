package com.github.martynfunclub.trackingsystem.repositories;

import com.github.martynfunclub.trackingsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.martynfunclub.trackingsystem.models.Change;

@Repository
public interface ChangeRepository extends JpaRepository<Change, Long> {
    Change findByUserAndEndTimeIsNull(User user);

}
