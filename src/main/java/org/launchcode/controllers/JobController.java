package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        model.addAttribute("job", jobData.findById(id));

        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("add", jobForm);
            return "new-job";
        }

        List<Employer> jobEmployers = jobData.getEmployers().findAll();
        Employer jobEmployer = jobEmployers.get(jobForm.getEmployerId());
        String jobName = jobForm.getName();
        Location jobLocation = jobForm.getLocation();
        PositionType jobType = jobForm.getPositionType();
        CoreCompetency jobCompetency = jobForm.getCoreCompetency();

        Job newJob = new Job();
        newJob.setName(jobName);
        newJob.setEmployer(jobEmployer);
        newJob.setLocation(jobLocation);
        newJob.setPositionType(jobType);
        newJob.setCoreCompetency(jobCompetency);
        jobData.add(newJob);

        model.addAttribute("job", jobData.findById(newJob.getId()));
        return "job-detail";

    }
}
