package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.dto.events.AddEventDto;
import com.softuni.crossfitapp.domain.dto.events.EventDetailsDto;
import com.softuni.crossfitapp.service.EventService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

import static com.softuni.crossfitapp.util.Constants.BINDING_RESULT_PATH;
import static com.softuni.crossfitapp.util.Constants.DOT;

@Controller
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events/publish-event")
    public String getEventPage( Model model){
        if(!model.containsAttribute("eventDetailsDto")){
            model.addAttribute("eventDetailsDto",new AddEventDto());
        }
        return "add-event";
    }

    @PostMapping("/events/publish-event")
    public ModelAndView postAnEvent(@Valid AddEventDto eventDetailsDto , BindingResult bindingResult, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView();
        if(bindingResult.hasErrors()){
            String attributeName = "eventDetailsDto";
            redirectAttributes
                    .addFlashAttribute("eventDetailsDto", eventDetailsDto)
                    .addFlashAttribute(BINDING_RESULT_PATH + DOT + attributeName, bindingResult);
            modelAndView.setViewName("redirect:/events/publish-event");
            return modelAndView;
        }
        eventService.addEvent(eventDetailsDto);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}
