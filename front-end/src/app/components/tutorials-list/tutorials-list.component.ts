import {Component, OnInit} from '@angular/core';
import {Tutorial} from "../../models/tutorial.model";
import {TutorialService} from "../../services/tutorial.service";

@Component({
  selector: 'app-tutorials-list',
  templateUrl: './tutorials-list.component.html',
  styleUrls: ['./tutorials-list.component.css']
})
export class TutorialsListComponent implements OnInit{

  tutorials?: Tutorial[];
  currentTutorial: Tutorial = {};
  currentIndex: number = -1;
  title: string = '';

  constructor(private tutorialService: TutorialService) { }



  ngOnInit(): void {
    this.retrieveTutorials();
  }

  retrieveTutorials(): void{
    this.tutorialService.getAll().subscribe({
      next: (data) => {
        this.tutorials = data;
        console.log(data);
      },
      error: (err) => console.error(err)
    });
  }

  refreshList(): void{
    this.retrieveTutorials();
    this.currentTutorial = {};
    this.currentIndex = -1;
  }

  setActiveTutorial(tutorial: Tutorial, idx: number){
    this.currentTutorial = tutorial;
    this.currentIndex = idx;
  }

  removeAllTutorials(): void{
    this.tutorialService.deleteAll().subscribe({
      next: (response) => {
        console.log(response);
        this.refreshList();
      },
      error: (err) => console.error(err)
    });
  }

  searchTitle():void {
    this.currentTutorial = {};
    this.currentIndex = -1;

    this.tutorialService.findByTitle(this.title).subscribe({
      next: (data) => {
        this.tutorials = data;
        console.log(data);
      },
      error: (err) => console.error(err)
    });
  }

}
