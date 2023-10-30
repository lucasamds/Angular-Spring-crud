import {Component, OnInit} from '@angular/core';
import {Tutorial} from "../../models/tutorial.model";
import {TutorialService} from "../../services/tutorial.service";

@Component({
  selector: 'app-add-tutorial',
  templateUrl: './add-tutorial.component.html',
  styleUrls: ['./add-tutorial.component.css']
})
export class AddTutorialComponent implements OnInit{

  tutorial: Tutorial = {
    title: '',
    description: '',
    published: false
  }

  submited:boolean = false;

  constructor(private tutorialService: TutorialService) { }

  ngOnInit(): void{
  }

  saveTutorial(): void{
    const data = {
      title: this.tutorial.title,
      description: this.tutorial.description
    };

    this.tutorialService.create(data).subscribe({
      next: (response) => {
        console.log(response);
        this.submited = true;
      },
      error: (err) => console.error(err)
    });
  }

  newTutorial(): void{
    this.submited = false;
    this.tutorial = {
      title: '',
      description: '',
      published: false
    };
  }

}
