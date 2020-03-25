// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.angular2.metadata;

import com.intellij.codeInspection.htmlInspections.HtmlUnknownAttributeInspection;
import com.intellij.codeInspection.htmlInspections.HtmlUnknownTagInspection;
import com.intellij.util.containers.ContainerUtil;
import org.angular2.Angular2CodeInsightFixtureTestCase;
import org.angular2.inspections.Angular2TemplateInspectionsProvider;
import org.angular2.inspections.AngularAmbiguousComponentTagInspection;
import org.angular2.inspections.AngularUndefinedBindingInspection;
import org.angular2.inspections.AngularUndefinedTagInspection;
import org.angularjs.AngularTestUtil;

public class IvyMetadataTest extends Angular2CodeInsightFixtureTestCase {

  @Override
  protected String getTestDataPath() {
    return AngularTestUtil.getBaseTestDataPath(getClass()) + "/ivy";
  }

  public void testInterModuleExtends() {
    myFixture.copyDirectoryToProject("ng-zorro", ".");
    myFixture.enableInspections(HtmlUnknownAttributeInspection.class,
                                AngularUndefinedBindingInspection.class);
    myFixture.configureFromTempProjectFile("inter_module_props.html");
    myFixture.checkHighlighting(true, false, true);
  }

  public void testMixedMetadataResolution() {
    //Test component matching and indirect node module indexing
    myFixture.copyDirectoryToProject("material", ".");
    myFixture.enableInspections(AngularAmbiguousComponentTagInspection.class,
                                AngularUndefinedTagInspection.class);
    myFixture.configureFromTempProjectFile("module.ts");
    myFixture.checkHighlighting();
    AngularTestUtil.moveToOffsetBySignature("mat-form<caret>-field", myFixture);
    assertEquals("form-field.d.ts",
                 myFixture.getElementAtCaret().getContainingFile().getName());
  }


  public void testIonicMetadataResolution() {
    myFixture.copyDirectoryToProject("@ionic", ".");
    myFixture.enableInspections(AngularAmbiguousComponentTagInspection.class,
                                AngularUndefinedTagInspection.class,
                                AngularUndefinedBindingInspection.class,
                                HtmlUnknownTagInspection.class,
                                HtmlUnknownAttributeInspection.class);
    myFixture.configureFromTempProjectFile("tab1.page.html");
    myFixture.checkHighlighting();
    AngularTestUtil.moveToOffsetBySignature("ion-card-<caret>subtitle", myFixture);
    assertEquals("proxies.d.ts",
                 myFixture.getElementAtCaret().getContainingFile().getName());
  }

  public void testFunctionPropertyMetadata() {
    myFixture.copyDirectoryToProject("function_property", ".");
    myFixture.enableInspections(new Angular2TemplateInspectionsProvider());
    myFixture.configureFromTempProjectFile("template.html");
    myFixture.checkHighlighting();
    assertEquals("my-lib.component.d.ts",
                 myFixture.getElementAtCaret().getContainingFile().getName());
  }

  public void testPriority() {
    myFixture.copyDirectoryToProject("priority", ".");
    myFixture.configureFromTempProjectFile("template.html");
    myFixture.completeBasic();
    assertEquals(ContainerUtil.newArrayList("comp-ivy-bar", "comp-ivy-foo", "comp-meta-bar"),
                 ContainerUtil.sorted(myFixture.getLookupElementStrings()));
  }

  public void testTransloco() {
    myFixture.enableInspections(new Angular2TemplateInspectionsProvider());
    myFixture.copyDirectoryToProject("transloco", ".");
    myFixture.configureFromTempProjectFile("transloco.html");
    myFixture.checkHighlighting();
  }

  public void testPureIvyConstructorAttribute() {
    myFixture.copyDirectoryToProject("pure-attr-support", ".");
    myFixture.enableInspections(new Angular2TemplateInspectionsProvider());
    myFixture.configureFromTempProjectFile("template.html");
    myFixture.checkHighlighting();
  }

}
