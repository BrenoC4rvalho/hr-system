import { Component } from "@angular/core"
import { LucideAngularComponent, LucideAngularModule, ScreenShareOff } from "lucide-angular";

@Component({
  selector: 'app-screen-size-restriction',
  imports: [LucideAngularModule],
  templateUrl: './screenSizeRestriction.component.html',
})
export class ScreenSizeRestrictionComponent {

  readonly screenSizeRestrictionIcon = ScreenShareOff

}
