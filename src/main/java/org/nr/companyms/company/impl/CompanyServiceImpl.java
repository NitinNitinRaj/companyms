package org.nr.companyms.company.impl;


import jakarta.ws.rs.NotFoundException;
import org.nr.companyms.company.Company;
import org.nr.companyms.company.CompanyRepository;
import org.nr.companyms.company.CompanyService;
import org.nr.companyms.company.clients.ReviewClient;
import org.nr.companyms.company.dto.ReviewMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private CompanyRepository companyRepository;
    private ReviewClient reviewClient;

    public CompanyServiceImpl(CompanyRepository companyRepository, ReviewClient reviewClient) {
        this.companyRepository = companyRepository;
        this.reviewClient = reviewClient;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public boolean updateCompany(Company company, Long id) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company companyToUpdate = companyOptional.get();
            companyToUpdate.setDescription(company.getDescription());
            companyToUpdate.setName(company.getName());
            companyRepository.save(companyToUpdate);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateCompanyRating(ReviewMessage reviewMessage) {
        Double averageRatingForCompany = reviewClient.getAverageRatingForCompany(reviewMessage.getCompanyId());
        Company company = companyRepository.findById(reviewMessage.getCompanyId()).orElseThrow(() -> new NotFoundException("Comapny not found: " + reviewMessage.getCompanyId()));
        company.setRating(averageRatingForCompany);
        companyRepository.save(company);
    }

    @Override
    public void createCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public boolean deleteCompanyById(Long id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

}
